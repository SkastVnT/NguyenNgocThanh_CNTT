from flask import Flask, render_template, url_for, redirect, request
import os
import pandas as pd
from datetime import datetime
from sklearn.preprocessing import LabelEncoder
from sklearn.svm import SVC
import librosa
import numpy as np
import pyaudio
import wave
from sklearn.model_selection import train_test_split
from flask_sqlalchemy import SQLAlchemy
from flask_login import UserMixin, login_user, LoginManager, login_required, logout_user, current_user
from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, SubmitField
from wtforms.validators import InputRequired, Length, ValidationError
from flask_bcrypt import Bcrypt

app = Flask(__name__)

# Configuration
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///database.db'
app.config['SECRET_KEY'] = 'thisisasecretkey'
model = None
le = None
attendance_df = pd.DataFrame(columns=['Time', 'Name', 'Điểm danh'])
# Initialize extensions
db = SQLAlchemy(app)
bcrypt = Bcrypt(app)

login_manager = LoginManager()
login_manager.init_app(app)
login_manager.login_view = 'login'

def record_audio(output_filename, duration=5, rate=44100, channels=1):
    p = pyaudio.PyAudio()
    stream = p.open(format=pyaudio.paInt16,
                    channels=channels,
                    rate=rate,
                    input=True,
                    frames_per_buffer=1024)

    frames = []

    print("Recording...")
    for _ in range(0, int(rate / 1024 * duration)):
        data = stream.read(1024)
        frames.append(data)
    print("Finished recording.")

    stream.stop_stream()
    stream.close()
    p.terminate()

    wf = wave.open(output_filename, 'wb')
    wf.setnchannels(channels)
    wf.setsampwidth(p.get_sample_size(pyaudio.paInt16))
    wf.setframerate(rate)
    wf.writeframes(b''.join(frames))
    wf.close()

def extract_features(file_name):
    try:
        audio, sample_rate = librosa.load(file_name)
        mfccs = librosa.feature.mfcc(y=audio, sr=sample_rate, n_mfcc=40)
        mfccs_scaled = np.mean(mfccs.T, axis=0)
        return mfccs_scaled
    except Exception as e:
        print(f"Error encountered while parsing file: {file_name}")
        return None

def train_model(audio_dir):
    X = []
    y = []
    le = LabelEncoder()

    for file in os.listdir(audio_dir):
        if file.endswith('.wav'):
            feature = extract_features(os.path.join(audio_dir, file))
            if feature is not None:
                X.append(feature)
                y.append(file.split('_')[0])

    X = np.array(X)
    y = le.fit_transform(np.array(y))

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    model = SVC(kernel='linear', probability=True)
    model.fit(X_train, y_train)

    train_accuracy = model.score(X_train, y_train)
    test_accuracy = model.score(X_test, y_test)

    return model, le, train_accuracy, test_accuracy

def recognize_speaker(filename, model, le):
    feature = extract_features(filename).reshape(1, -1)
    if feature is None:
        print("Feature extraction failed.")
        return None

    prediction = model.predict(feature)
    probabilities = model.predict_proba(feature)
    speaker = le.inverse_transform(prediction)[0]
    print(f"Predicted speaker: {speaker}, Probabilities: {probabilities}, prediction:{prediction}")
    return speaker

@app.route('/record', methods=['POST'])
def record():
    name = request.form['name']
    filename = f"recordings/{name}.wav"
    record_audio(filename, duration=5)
    return render_template('dashboard.html', message=f"Recording saved as {filename}")

@app.route('/train', methods=['GET'])
def train():
    global model, le, attendance_df
    audio_dir = "recordings"
    model, le, train_accuracy, test_accuracy = train_model(audio_dir)
    return render_template('home.html', message=f"Model trained successfully. Train Accuracy: {train_accuracy * 100:.2f}%, Test Accuracy: {test_accuracy * 100:.2f}%")

@app.route('/recognize', methods=['POST'])
def recognize():
    global model, le, attendance_df
    if model is None or le is None:
        return render_template('home.html', message="Vui lòng huấn luyện mô hình trước.")

    filename = "test.wav"
    record_audio(filename, duration=5)

    if not os.path.exists(filename):
        return render_template('home.html', message="Ghi âm thất bại. Tệp không được tạo.")

    speaker = recognize_speaker(filename, model, le)

    if speaker is not None:
        time_now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        new_row = pd.DataFrame({'Time': [time_now], 'Name': [speaker], 'Điểm danh': ['Đã Điểm Danh']})
        attendance_df = pd.concat([attendance_df, new_row], ignore_index=True)
        attendance_df.to_excel("attendance.xlsx", index=False)
        message = f"Đã nhận diện người nói: {speaker}. Điểm danh thành công."
    else:
        message = "Không nhận diện được người nói."

    return render_template('home.html', message=message)

@login_manager.user_loader
def load_user(user_id):
    return User.query.get(int(user_id))

class User(db.Model, UserMixin):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), nullable=False, unique=True)
    password = db.Column(db.String(80), nullable=False)
    attendance = db.relationship('Attendance', backref='user', lazy=True)

class Attendance(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    time = db.Column(db.DateTime, nullable=False, default=datetime.utcnow)
    status = db.Column(db.String(20), nullable=False)

    def __repr__(self):
        return f"Attendance('{self.user_id}', '{self.time}', '{self.status}')"

class RegisterForm(FlaskForm):
    username = StringField(validators=[
                           InputRequired(), Length(min=4, max=20)], render_kw={"placeholder": "Username"})

    password = PasswordField(validators=[
                             InputRequired(), Length(min=8, max=20)], render_kw={"placeholder": "Password"})

    submit = SubmitField('Register')

    def validate_username(self, username):
        existing_user_username = User.query.filter_by(
            username=username.data).first()
        if existing_user_username:
            raise ValidationError(
                'That username already exists. Please choose a different one.')

class LoginForm(FlaskForm):
    username = StringField(validators=[
                           InputRequired(), Length(min=4, max=20)], render_kw={"placeholder": "Username"})

    password = PasswordField(validators=[
                             InputRequired(), Length(min=8, max=20)], render_kw={"placeholder": "Password"})

    submit = SubmitField('Login')

@app.route('/')
def home():
    return render_template('home.html')

@app.route('/login', methods=['GET', 'POST'])
def login():
    form = LoginForm()
    if form.validate_on_submit():
        user = User.query.filter_by(username=form.username.data).first()
        if user:
            if bcrypt.check_password_hash(user.password, form.password.data):
                login_user(user)
                return redirect(url_for('dashboard'))
    return render_template('login.html', form=form)

@app.route('/dashboard', methods=['GET', 'POST'])
@login_required
def dashboard():
    return render_template('dashboard.html')

@app.route('/logout', methods=['GET', 'POST'])
@login_required
def logout():
    logout_user()
    return redirect(url_for('login'))

@app.route('/register', methods=['GET', 'POST'])
def register():
    form = RegisterForm()

    if form.validate_on_submit():
        hashed_password = bcrypt.generate_password_hash(form.password.data).decode('utf-8')
        new_user = User(username=form.username.data, password=hashed_password)
        db.session.add(new_user)
        db.session.commit()
        return redirect(url_for('login'))

    return render_template('register.html', form=form)

if __name__ == "__main__":
    with app.app_context():
        db.create_all()
        # Train model automatically on startup
        audio_dir = "recordings"
        model, le, train_accuracy, test_accuracy = train_model(audio_dir)
        print(f"Model trained successfully. Train Accuracy: {train_accuracy * 100:.2f}%, Test Accuracy: {test_accuracy * 100:.2f}%")
    app.run(debug=True)

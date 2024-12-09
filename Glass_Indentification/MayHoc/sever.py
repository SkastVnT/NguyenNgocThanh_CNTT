# pip install Flask
# pip install flask-cors
# pip install scikit-learn
import numpy as np
import pickle
from flask import Flask, request, jsonify
app = Flask(__name__)
from flask_cors import CORS
CORS (app)
# load the model
model = pickle.load(open('model.pkl', 'rb'))
# apis
@app.route('/linear-regression/height-weight', methods=['POST'])
def heightweight ():
  # get the data from the POST request
  data = request.get_json(force=True)
  # make prediction using model loaded from disk as per the data
  p_test = data['height']
  X_test = np.array([p_test])
  y_test_hat = model.predict(X_test)
  # take the first value of prediction
  output = y_test_hat [0]
  return jsonify (output)
# start server
if __name__ == '__main__':
  app.run(port=5000, debug=True)
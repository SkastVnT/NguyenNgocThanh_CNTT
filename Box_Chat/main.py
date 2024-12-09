import networkx as nx
from vncorenlp import VnCoreNLP
from sklearn.metrics.pairwise import cosine_similarity
from transformers import BertTokenizer, BertModel
import torch
import numpy as np

# Đường dẫn đến VnCoreNLP
vncorenlp_path = 'D:/HocTap/ChatBot/VnCoreNLP-master/VnCoreNLP-master/VnCoreNLP-1.1.1.jar'
rdrsegmenter = VnCoreNLP(vncorenlp_path, annotators="wseg,pos,ner,parse", max_heap_size='-Xmx2g')


# Hàm phân tích câu hỏi
def analyze_sentence(sentence):
    annotations = rdrsegmenter.annotate(sentence)
    detailed_info = []
    predicate_position = None  # Thêm biến này để lưu vị trí của predicate
    predicate = []
    found_verb = False
    stop_predicate = False
    verb_count = 0  # Counter for verbs
    noun_phrases = []
    temp_noun_phrases = []
    current_noun_phrase = []
    in_predicate = False  # Flag to track if we are inside the predicate

    question_type = None
    wh_type = None
    wh_word = None
    wh_phrase = None

    wh_questions = {
        "người": ["Ai", "ai"],
        "thing": ["gì", "Cái gì", "cái gì", "nào"],
        "place": ["Ở đâu"],
        "time": ["Khi nào", "Lúc nào"],
        "manner": ["Như thế nào"],
        "reason": ["Tại sao", "Vì sao"],
        "quantity": ["Bao nhiêu", "Mấy"],
        "condition": ["Thế nào"]
    }
    yes_no_keywords = ["có", "là", "phải", "đúng", "không"]

    for sent in annotations['sentences']:
        for token in sent:
            word = token['form']
            pos = token['posTag']

            detailed_info.append((word, pos))

            # Xác định vị trí của predicate
            if (pos == 'E' or pos == 'V') and verb_count == 0:
                verb_count += 1  # Increment verb counter
                if not found_verb:
                    predicate.append(word)
                    in_predicate = True  # Set the flag when verb is found
                    predicate_position = len(detailed_info) - 1  # Lưu vị trí của predicate
                found_verb = True
            elif found_verb and not stop_predicate:
                predicate.append(word)
                if (pos == 'E' or pos == 'V') and verb_count > 0:  # End predicate on 'E' or 'V'
                    stop_predicate = True
                    in_predicate = False  # Reset the flag when ending predicate

            # Xác định vị trí của noun phrase
            if pos == 'N' or pos == 'Np':
                if in_predicate:  # Nếu đang ở trong predicate
                    temp_noun_phrases.append((word, len(detailed_info)))  # Lưu vị trí của noun phrase
                else:
                    current_noun_phrase.append((word, len(detailed_info)))  # Lưu vị trí của noun phrase
            elif word == '-' and current_noun_phrase:
                if in_predicate:
                    temp_noun_phrases.append((word, len(detailed_info)))  # Lưu vị trí của noun phrase
                else:
                    current_noun_phrase.append((word, len(detailed_info)))  # Lưu vị trí của noun phrase
            else:
                if current_noun_phrase:
                    noun_phrases.append(current_noun_phrase)  # Lưu noun phrase vào danh sách
                    current_noun_phrase = []

                if temp_noun_phrases:
                    if stop_predicate:
                        temp_noun_phrases = []
                    else:
                        noun_phrases.append(temp_noun_phrases)  # Lưu noun phrase vào danh sách
                        temp_noun_phrases = []

    # Add last noun phrase if any
    if current_noun_phrase:
        noun_phrases.append(current_noun_phrase)

    if temp_noun_phrases and not stop_predicate:
        noun_phrases.append(temp_noun_phrases)

    if not stop_predicate:
        predicate = predicate[:1]

    predicate_text = ' '.join(predicate) if predicate else None

    for word, pos in detailed_info:
        if question_type is None:
            for qtype, keywords in wh_questions.items():
                if word in keywords:
                    question_type = "Wh-question"
                    wh_type = qtype
                    wh_word = word
                    break

    if question_type is None:
        for word, pos in detailed_info:
            if word in yes_no_keywords:
                question_type = "Yes/No question"
                break

    if wh_word == "nào":
        for i, (word, pos) in enumerate(detailed_info):
            if word == "nào":
                j = i

                j = i - 1
                wh_phrase = []
                while j >= 0 and (
                        detailed_info[j][1] == 'N' or detailed_info[j][1] == 'Np' or detailed_info[j][0] == '-'):
                    wh_phrase.insert(0, detailed_info[j][0])
                    j -= 1
                wh_phrase = " ".join(wh_phrase)
                break

    detailed_info = [(word.replace("_", " "), pos) for word, pos in detailed_info]
    noun_phrases = [[(word.replace("_", " "), pos) for word, pos in phrase] for phrase in noun_phrases]  # Xử lý noun phrases
    predicate_text = predicate_text.replace("_", " ") if predicate_text else None
    wh_word = wh_word.replace("_", " ") if wh_word else None
    if wh_word == "nào" and wh_phrase:
        wh_phrase = wh_phrase.replace("_", " ")

    return detailed_info, predicate_text, noun_phrases, question_type, wh_type, wh_word, wh_phrase, predicate_position


# Đọc triple.txt và tạo Knowledge Graph
def read_triples(file_path):
    triples = []
    with open(file_path, 'r', encoding='utf-8') as file:
        for line in file:
            line = line.strip()
            if line:
                parts = line.split('\t')
                if len(parts) == 3:
                    head, predicate, tail = parts
                    head_entity, head_type = head.split(':')
                    tail_entity, tail_type = tail.split(':')
                    triples.append((head_entity, head_type, predicate, tail_entity, tail_type))
    return triples


def create_knowledge_graph(triples):
    kg = nx.DiGraph()

    for head_entity, head_type, predicate, tail_entity, tail_type in triples:
        kg.add_node(head_entity, type=head_type)
        kg.add_node(tail_entity, type=tail_type)
        kg.add_edge(head_entity, tail_entity, predicate=predicate)

    return kg


# Load pre-trained BERT model and tokenizer
tokenizer = BertTokenizer.from_pretrained('bert-base-multilingual-cased')
model = BertModel.from_pretrained('bert-base-multilingual-cased')


# Embedding function
def embed_sentence(sentence):
    inputs = tokenizer(sentence, return_tensors='pt', truncation=True, padding=True)
    outputs = model(**inputs)
    return outputs.last_hidden_state.mean(dim=1).detach().numpy()


# Main function to process the question
def process_question(sentence, kg):
    details, predicate, noun_phrases, question_type, wh_type, wh_word, wh_phrase, predicate_position = analyze_sentence(sentence)

    question_vector = embed_sentence(sentence)
    high_similarity_sentences = []

    related_nodes_before_predicate = []
    related_nodes_after_predicate = []
    for noun in noun_phrases:
        for node in kg.nodes:
            if noun[-1][0].lower() in node.lower():
                if noun[-1][1] > predicate_position:
                    related_nodes_after_predicate.append(node)
                else:
                    related_nodes_before_predicate.append(node)

    for node in related_nodes_before_predicate:
        neighbors = list(kg.neighbors(node))  # Danh sách các node liền kề của node hiện tại
        for neighbor in neighbors:
            if kg.nodes[neighbor]['type'].lower() == wh_type.lower():
                # Lưu lại câu gốc
                original_sentence = f"{node} {kg.edges[node, neighbor]['predicate']} {neighbor}"

                # Tạo câu thay thế bằng cách thay thế node gần kề với từ WH
                if wh_word == "nào" and wh_phrase:
                    replacement_sentence = original_sentence.replace(neighbor, f"{wh_phrase} {wh_word}")
                else:
                    replacement_sentence = original_sentence.replace(neighbor, wh_word)

                # Tính độ tương đồng cosine
                replacement_vector = embed_sentence(replacement_sentence)
                similarity = \
                cosine_similarity(question_vector.reshape(1, -1), replacement_vector.reshape(1, -1)).flatten()[0]

                # Nếu độ tương đồng > 0.97, lưu lại triple gốc và câu thay thế
                if similarity > 0.97:
                    high_similarity_sentences.append((original_sentence, replacement_sentence))

    for node in related_nodes_after_predicate:
        neighbors = list(kg.predecessors(node))  # Danh sách các node liền kề của node hiện tại
        for neighbor in neighbors:
            if kg.nodes[neighbor]['type'].lower() == wh_type.lower():
                # Lưu lại câu gốc
                original_sentence = f"{neighbor} {kg.edges[neighbor, node]['predicate']} {node}"

                # Tạo câu thay thế bằng cách thay thế node gần kề với từ WH
                if wh_word == "nào" and wh_phrase:
                    replacement_sentence = original_sentence.replace(neighbor, f"{wh_phrase} {wh_word}")
                else:
                    replacement_sentence = original_sentence.replace(neighbor, wh_word)

                # Tính độ tương đồng cosine
                replacement_vector = embed_sentence(replacement_sentence)
                similarity = \
                cosine_similarity(question_vector.reshape(1, -1), replacement_vector.reshape(1, -1)).flatten()[0]

                # Nếu độ tương đồng > 0.97, lưu lại triple gốc và câu thay thế
                if similarity > 0.97:
                    high_similarity_sentences.append((original_sentence, replacement_sentence))

    if high_similarity_sentences:
        print("Original triples and replacement sentences for high similarity sentences:")
        for original_sentence, replacement_sentence in high_similarity_sentences:
            print(f"Original Sentence: {original_sentence}")
            print(f"Replacement Sentence: {replacement_sentence}")
    else:
        print("No suitable match found")


if __name__ == "__main__":
    triples = read_triples('D:/HocTap/ChatBot/Triple.txt')
    kg = create_knowledge_graph(triples)
    sentence = "Khoa Công nghệ thông tin có trưởng khoa là ai"
    # sentence = "Ai là trưởng khoa của khoa Công nghệ thông tin"

    # Phân tích câu hỏi và in thông tin chi tiết
    details, predicate, noun_phrases, question_type, wh_type, wh_word, wh_phrase, predicate_position = analyze_sentence(sentence)
    print("Details:", details)
    print("Predicate:", predicate)
    print("Noun Phrases:", noun_phrases)
    print("Question Type:", question_type)
    print("Wh Type:", wh_type)
    print("Wh Word:", wh_word)
    print("Wh Phrase:", wh_phrase)

    # Xử lý câu hỏi và in triple gốc cho các câu có độ tương đồng cao
    if question_type == "Wh-question":
        process_question(sentence, kg)
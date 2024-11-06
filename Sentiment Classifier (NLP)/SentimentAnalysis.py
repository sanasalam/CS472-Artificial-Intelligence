import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import re

import nltk
from nltk.corpus import stopwords 
from nltk.stem import WordNetLemmatizer

from sklearn.feature_extraction.text import CountVectorizer
from sklearn.naive_bayes import BernoulliNB
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report

df_train = pd.read_csv("train.txt",delimiter=';',names=['phrase','sentiment'])

def encoder(df):
    df.replace(to_replace ="surprise", value =1, inplace=True) #Group surprise, love, and joy emotions under positive emotion
    df.replace(to_replace ="love", value =1, inplace=True)
    df.replace(to_replace ="joy", value =1, inplace=True)
    df.replace(to_replace ="fear", value =0, inplace=True) #Group fear, anger, and sadness emotions under negative emotion
    df.replace(to_replace ="anger", value =0, inplace=True)
    df.replace(to_replace ="sadness", value =0, inplace=True)

encoder(df_train['sentiment']) 

lm = WordNetLemmatizer()

def preprocessing(df_col):
    corpus = []
    for item in df_col:
        new_item = re.sub('[^a-zA-Z]',' ',str(item)) #Remove non-alphabetic characters from the phrase
        new_item = new_item.lower() #Convert string to lowercase
        new_item = new_item.split() #Convert string to a list of words
        new_item = [lm.lemmatize(word) for word in new_item if word not in set(stopwords.words('english'))] #Remove stopwords & lemmatize remaining words
        corpus.append(' '.join(str(x) for x in new_item)) #Revert list to string
    return corpus

corpus = preprocessing(df_train['phrase']) 

cv = CountVectorizer() #Convert 
x = cv.fit_transform(corpus) #Convert text to vectors for model training
y = df_train.sentiment

X_train, X_test, y_train, y_test = train_test_split(x, y, test_size=0.20, random_state=0) #Split dataset into training and testing data
bnb = BernoulliNB(binarize=0.0) #Instantiate Bernoulli Naive Bayes model for binary classification 
model = bnb.fit(X_train, y_train) #Train the model 

y_pred = bnb.predict(X_test) #Evaluate the model against the test data

print(classification_report(y_test, y_pred)) 
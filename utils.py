import xml.etree.cElementTree as ET
import sys
import re
import string
import nltk
from sklearn.feature_extraction.text import ENGLISH_STOP_WORDS
from nltk import PorterStemmer
from collections import Counter


def tokenize(text):
    text=text.lower()
    junk=re.compile('[' + string.punctuation + '0-9\\r\\t\\n]')
    text=re.sub(junk," ",text)
    tokens=[]
    tokens=nltk.word_tokenize(text)
    tokens_new = [w for w in tokens if len(w) >= 3]
    stop_words=ENGLISH_STOP_WORDS
    filtered_words=[w for w in tokens_new if w not in stop_words]
    return filtered_words


def stemwords(words):
    stemmer=PorterStemmer()
    stem_words=[stemmer.stem(w) for w in words]
    return stem_words

import sys
from nltk.tokenize import RegexpTokenizer
from stop_words import get_stop_words
from nltk.stem.porter import PorterStemmer
from gensim import corpora, models
import gensim
import nltk
import string
import os
import codecs
from utils import *

path = sys.argv[1]

doc_set=[] #list of lists where each list is a text from the document
for subdir, dirs, files in os.walk(path):
    for file in files:
        file_path = subdir + os.path.sep + file
        shakes = codecs.open(file_path, 'r', encoding='utf-8')
        text = shakes.read()
        doc_set.append(text)

texts = []

for i in doc_set:
    tokens = stemwords(tokenize(i))
    texts.append(tokens)

# turn our tokenized documents into a id <-> term dictionary
dictionary = corpora.Dictionary(texts)

# convert tokenized documents into a document-term matrix
corpus = [dictionary.doc2bow(text) for text in texts]

# generate LDA model
ldamodel = gensim.models.ldamodel.LdaModel(corpus, num_topics=5, id2word = dictionary, passes=20, minimum_probability=0)

for x in ldamodel.get_document_topics(corpus, minimum_phi_value=None, per_word_topics=False):
    print(x)

#for i in range(0, ldamodel.num_topics-1):
#    print (ldamodel.print_topic(i))




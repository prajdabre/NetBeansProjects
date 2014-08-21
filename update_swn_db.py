# -*- coding: utf-8 -*-


#Please not that intensive list operations have been used
# DICTIONARIES , MAPS , LAMBDA FUNCTIONS , ITERATORS ets are used
# Note that when using a dictionary the ordering of keys may change from time to time as and when you edit the content (not add/remove but modify)
# so i had to use iterators to sort the keys and then modify
# This was fun but frustrating but fun 
# My advice - Unless you are well versed in python or have understood my code , do not attempt to uncomment any part of the code or modify it .
# You can uncomment the print statements to see the content but frankly as long as you get the output dont bother with it unless you actually want to 	understand the code and make it better.

# Author of this story : Raj Dabre from IIT Bombay

import itertools
import MySQLdb
import sys
hinfile=open(sys.argv[1])

hinarr=hinfile.readlines()
key = lambda sep: sep == '\n'

hinarrsplit=[list(group) for is_key, group in itertools.groupby(hinarr, key) if not is_key]  # This splits the synsets into separate groups

db = MySQLdb.connect("localhost","dipak","tmp123","swn_web_unicode" )
cursor = db.cursor()

for i in hinarrsplit:
	#print i
	if len(i)!=5:
		continue
	words_ind= i[4].split('::')[1].split(',')
	words_str = i[4].split('::')[1].strip()
	category = i[1].split('::')[1].strip().lower()
	gloss = i[2].split('::')[1].strip()
	example = i[3].split('::')[1].strip()
	sid = i[0].split('::')[1].strip()
	#print words_ind
	#print words_str
	#print category
	#print gloss
	#print sid
	#print example
	query="SELECT * from tbl_all_synset where synset_id="+sid
	cursor.execute(query)
	res = cursor.fetchall()
	if res.__len__()==0:
		try:
			print "Inserting for "+sid
			query="INSERT INTO tbl_all_synset VALUES("+sid+",'','"+words_str+"','"+gloss+"; "+example+"','"+category+"')";
			cursor.execute(query)
			
			db.commit()
			for word in words_ind:
				query="INSERT INTO tbl_all_words VALUES("+sid+",'"+word+"','"+category+"',999)";
				cursor.execute(query)
				db.commit()
			db.commit()
		except MySQLdb.Error, e:
			print str(e.args[0])+" "+e.args[1]
			db.rollback()
		
			
	else:
		try:
			#query="UPDATE tbl_all_synset SET synset='"+words_str+"', gloss='"+gloss+"; "+example+"', category='"+category+"' where synset_id="+int(sid);
			query = "DELETE * from tbl_all_synset where synset_id="+sid
			cursor.execute(query)
			db.commit()
			query="INSERT INTO tbl_all_synset VALUES("+sid+",'','"+words_str+"','"+gloss+"; "+example+"','"+category+"')";
			cursor.execute(query)
			db.commit()
			query = "DELETE * from tbl_all_words where synset_id="+sid
			cursor.execute(query)
			db.commit()
			for word in words_ind:
				query="INSERT INTO tbl_all_words VALUES("+sid+",'"+word+"','"+category+"',999)";
				cursor.execute(query)
				db.commit()
			db.commit()
		except:
			db.rollback()

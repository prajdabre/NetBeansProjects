import sys
import csv
if len(sys.argv)!=2:
    print "Incorrect number of arguments entered\n Usage SCRIPTNAME CSVFILE.csv"
else:
    if sys.argv[1].split('.')[1] is 'csv':
        csvdata=csv.reader(open(sys.argv[1]))
        a=[]
        for i in range(2):
            csvdata.next()
        for row in csvdata:
            a.append(row)
        print "The total number of candidates in the program are",len(a)
    else:
        print "You have not specfied a csv file"
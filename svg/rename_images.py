#!/usr/bin/env python
# -*- coding: latin-1 -*-
import json, sys
from collections import OrderedDict
import codecs, os

#print 'Number of arguments:', len(sys.argv), 'arguments.'
#print 'Argument List:', str(sys.argv)

if len(sys.argv) != 2:
	print "Usage: number_units input_file"
	sys.exit(1)

filename = sys.argv[1]

print "Reading from: " + filename
with open(filename, 'r') as f:
     data = json.load(f, object_pairs_hook=OrderedDict)
     
#print data


for i in range(len(data)):
	name = data[i]["isc"]
	faction = data[i]["army"]
	id = str(data[i]["id"]) + ".svg"
	resourcename = faction + "_" + name

	resourcename = resourcename.lower().replace(' ','_').replace('-','_').replace(',','').replace(':','').replace('.','').replace('\'','').replace('(','').replace(')','');		
	resourcename = resourcename + ".svg"
	#print "Filename: " + resourcename + " = " + str(id)

	if os.path.isfile(resourcename):
		os.rename(resourcename, id)


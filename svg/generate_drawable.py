import re, glob, os, sys, array
from subprocess import call

#print 'Number of arguments:', len(sys.argv), 'arguments.'
#print 'Argument List:', str(sys.argv)

if len(sys.argv) != 3:
	print "Usage: generate_drawable [size] [files]"
	sys.exit(1)
	
files = sys.argv[2] 
size = sys.argv[1]
size_float = float(size)

print files + " " + size + "px"

# standard definitions for size and folder
sizes = 	[size_float * 0.75, size_float,      size_float * 1.5, size_float * 2,   size_float * 3]
sizedirs =	["drawable-ldpi",   "drawable-mdpi", "drawable-hdpi",  "drawable-xhdpi", "drawable-xxhdpi"] 

# create the destination folders if they don't exist
for x in range(0, len(sizedirs)):
	if not os.path.exists(sizedirs[x]):
    		os.makedirs(sizedirs[x])
    		print "Creating directory: " + sizedirs[x]


# loop through the directory and convert each file
for pathname in glob.glob(files):
	basename= os.path.basename(pathname)
	for x in range(0, len(sizedirs)):
		os.system("inkscape -w " + str(sizes[x]) + " -e \"" + sizedirs[x] + "\\" + os.path.splitext(basename)[0] + "_" + size + ".png\" \"" + basename + "\"")
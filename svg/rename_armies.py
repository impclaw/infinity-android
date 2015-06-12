import re, glob, os, sys

	
#print 'Number of arguments:', len(sys.argv), 'arguments.'
#print 'Argument List:', str(sys.argv)

files = "*.svg" 
pattern = r"(^[\w\s]+[\w])\s-\sMain Logo.*" 
replacement = r"\1.svg" 

print files
print pattern
print replacement

for pathname in glob.glob(files):
	basename= os.path.basename(pathname)
	new_filename= re.sub(pattern, replacement, basename)
	if new_filename != basename:
		new_filename = new_filename.replace(" ", "_").lower()
		print "new_filename: " + new_filename
		os.rename(pathname, os.path.join(os.path.dirname(pathname), new_filename))



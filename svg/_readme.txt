Vector files by Vyo via this thread: infinitytheforums.com/forum/index.php?/topic/25308-3rd-edition-unit-logos-in-vector-format

And this repository: https://drive.google.com/folderview?id=0B9RXq5MF7KXuVm1GdFIzSDVBRjA&usp=sharing

The file names have been reformatted to match the ISC names in the JSON data files.

The file contents have been modified so that the image matches the canvas size and I can get nice square png's out.

C:\projects\Personal\Android-SVG-Asset-Generator\assets>"c:\Program Files\Inkscape\inkscape" --verb=FitCanvasToDrawing --verb=FileSave --verb=FileClose *.svg

C:\projects\Personal\Android-SVG-Asset-Generator\assets>"c:\Program Files\Inkscape\inkscape" -w 48 -e drawing.png "Aleph - Main Logo.svg"
Background RRGGBBAA: ffffff00
Area 0:0:566.929:566.929 exported to 48 x 48 pixels (7.62 dpi)
Bitmap saved as: drawing.png

C:\projects\Personal\Android-SVG-Asset-Generator\assets>"c:\Program Files\Inksca
pe\inkscape" -w 72 -e drawing72.png "Aleph - Main Logo.svg"
Background RRGGBBAA: ffffff00
Area 0:0:566.929:566.929 exported to 72 x 72 pixels (11.43 dpi)
Bitmap saved as: drawing72.png

C:\projects\Personal\Android-SVG-Asset-Generator\assets>



Lower case everything in a directory
for /f "Tokens=*" %f in ('dir /l/b/a-d') do (rename "%f" "%f")





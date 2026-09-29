#!/usr/bin/env python3
from pathlib import Path
from PIL import Image, ImageOps
from skimage.measure import find_contours
from fontTools.fontBuilder import FontBuilder
from fontTools.pens.ttGlyphPen import TTGlyphPen
import numpy as np

ROOT=Path(__file__).resolve().parents[1]
PNG=ROOT/"app/src/main/res/drawable-nodpi/ninja_sample.png"
OUT=ROOT/"app/src/main/res/font/pi_ninja.ttf"
EM=2048
MARGIN=128
if not PNG.exists(): raise SystemExit(f"Missing source PNG: {PNG}")
OUT.parent.mkdir(parents=True,exist_ok=True)
im=Image.open(PNG).convert("RGBA")
alpha=im.getchannel("A")
if alpha.getextrema()[1]==0: raise SystemExit("PNG has no visible pixels")
if alpha.getextrema()!=(255,255):
    mask=alpha
else:
    mask=ImageOps.grayscale(im).point(lambda p:255 if p<245 else 0)
bbox=mask.getbbox()
if not bbox: raise SystemExit("PNG contains no visible artwork")
mask=mask.crop(bbox)
max_side=256
scale=min(1.0,max_side/max(mask.size))
if scale<1:
    mask=mask.resize((max(1,round(mask.width*scale)),max(1,round(mask.height*scale))),Image.Resampling.LANCZOS)
binary=mask.point(lambda p:1 if p>=96 else 0)
contours=find_contours(np.array(binary,dtype=float),0.5)
w,h=binary.size
s=(EM-2*MARGIN)/max(w,h)
draw_w,draw_h=w*s,h*s
x0=(EM-draw_w)/2
y0=MARGIN
pen=TTGlyphPen(None)
for contour in contours:
    if len(contour)<3: continue
    pts=[]
    for row,col in contour:
        p=(round(x0+col*s),round(y0+(h-row)*s))
        if not pts or p!=pts[-1]: pts.append(p)
    if len(pts)<3: continue
    pen.moveTo(pts[0])
    for p in pts[1:]: pen.lineTo(p)
    pen.closePath()
glyph=pen.glyph()
notdef=TTGlyphPen(None).glyph()
fb=FontBuilder(EM,isTTF=True)
fb.setupGlyphOrder([".notdef","uniE000"])
fb.setupCharacterMap({0xE000:"uniE000"})
fb.setupGlyf({".notdef":notdef,"uniE000":glyph})
fb.setupHorizontalMetrics({".notdef":(EM,0),"uniE000":(EM,0)})
fb.setupHorizontalHeader(ascent=EM,descent=-256)
fb.setupOS2(sTypoAscender=EM,sTypoDescender=-256,usWinAscent=EM,usWinDescent=256,usWeightClass=400,usWidthClass=5)
fb.setupNameTable({"familyName":"Pi Ninja","styleName":"Regular","fullName":"Pi Ninja Regular","uniqueFontIdentifier":"Pi Ninja Regular","psName":"PiNinja-Regular","version":"Version 1.0"})
fb.setupPost()
fb.setupHead()
fb.save(OUT)
print(f"Generated {OUT} ({OUT.stat().st_size} bytes), U+E000")

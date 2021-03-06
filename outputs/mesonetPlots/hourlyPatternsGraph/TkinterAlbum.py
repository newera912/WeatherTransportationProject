#!/usr/bin/env python
from Tkinter import *
from PIL import Image
from PIL import ImageTk
import os, sys,time

class Viewer:
    def __init__(self, master, filelist):
        self.top = master
        self.files = filelist
        self.index = 0
        #display first image
        filename = filelist[0]
        if not os.path.exists(filename):
            print "Unable to find %s" % filename
            self.top.quit()

        self.title = Label(text=os.path.basename(filename))
        self.title.pack()
        
        im = Image.open(filename)
#         if im.format == "SPIDER":
#             im = im.convert2byte()
        self.size = im.size
#        self.tkimage = ImageTk.PhotoImage(im, palette=256)
        self.tkimage = ImageTk.PhotoImage(im)
        self.lbl = Label(master, image=self.tkimage)
        self.lbl.pack(side='top')

        # the button frame
        fr = Frame(master)
        fr.pack(side='top', expand=1, fill='x')
        
        back = Button(fr, text="<<Prev Day", command=lambda : self.nextframe(-24))
        back.grid(row=0, column=0, sticky="w", padx=4, pady=4)
        
        
        back = Button(fr, text="<back", command=lambda : self.nextframe(-1))
        back.grid(row=0, column=1, sticky="w", padx=4, pady=4)

        ilabel = Label(fr, text="image number:")
        ilabel.grid(row=0, column=2, sticky="e", pady=4)

        self.evar = IntVar()
        self.evar.set(1)
        entry = Entry(fr, textvariable=self.evar)
        entry.grid(row=0, column=3, sticky="w", pady=4)
        entry.bind('<Return>', self.title)
        
        next = Button(fr, text="next>", command=lambda : self.nextframe(1))
        next.grid(row=0, column=4, sticky="e", padx=4, pady=4)

        back = Button(fr, text="Next Day>>", command=lambda : self.nextframe(24))
        back.grid(row=0, column=5, sticky="w", padx=4, pady=4)
        
        back = Button(fr, text="Play ==>", command=lambda : self.play(1))
        back.grid(row=0, column=6, sticky="w", padx=4, pady=4)
    # image doesn't appear unless put Image.open in separate function?
    # and need to use tkimage.paste, not ImageTk.PhotoImage
    def getImage(self, filename):
        im = Image.open(filename)
        if im.format == "SPIDER":
            im = im.convert2byte()
        if im.size != self.size:
            print "all images must be same dimensions:"
            f1 = os.path.basename(self.files[0])
            f2 = os.path.basename(filename)
            print "%s: %s, %s : %s" % (f1, str(self.size),f2, str(im.size))
            self.top.quit()
        return im

    def nextframe(self,i=1, imgnum=-1):
        if imgnum == -1:
            self.index += i
        else:
            self.index = imgnum - 1
        if self.index >= len(self.files):
            self.index = 0
        elif self.index < 0:
            self.index = len(self.files) - 1
        filename = self.files[self.index]
        if not os.path.exists(filename):
            print "Unable to find %s" % filename
            self.top.quit()
        self.title.configure(text=os.path.basename(filename))
        self.evar.set(self.index+1)
        
        im = self.getImage(filename)
        self.tkimage.paste(im)
    def play(self,i=1, imgnum=-1):
        while(True):
            time.sleep(500)
            self.nextframe(1)
            
            
    def getimgnum(self, event=None):
        self.nextframe(imgnum=self.evar.get())

# --------------------------------------------------------------------
if __name__ == "__main__":
    root="./AllDays/"
    months=["201603","201604","201605","201606","201607","201608","201609"]
    days=["%02d" % (i+1,) for i in range(32)]
    
    filelist=[]
    for mon in months:
        for day in days:
            date=mon+day
            for h in range(24):
                fileName=root+date+"-hour-"+str(h)+".png"
                if os.path.exists(fileName):
                    filelist.append(fileName)
                else:
                    continue
    
    

    root = Tk()
    app = Viewer(root, filelist)
    root.mainloop()
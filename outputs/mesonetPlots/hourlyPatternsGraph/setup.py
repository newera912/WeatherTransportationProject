from distutils.core import setup
import py2exe, sys, os

sys.argv.append('py2exe')

setup(
    options = {'py2exe': {'compressed': True}},
    windows = [{'script': "TkinterAlbum.py"}],
    zipfile = None,
)
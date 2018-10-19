import os
if __name__ == '__main__':
    filee = os.path.realpath(__file__)
    fpath,fname = os.path.split(filee)
    projectFile = fpath.replace("/tools","/proto/")
    print(projectFile)
    ss = 'pbjs -t static-module -w commonjs -o ' + 'GameProtocol.js ' + projectFile +'GameProtocol.proto'
    ts = 'pbts -o GameProtocol.d.ts GameProtocol.js'
    os.system(ss)
    os.system(ts)
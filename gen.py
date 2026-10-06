import json,os,subprocess,sys
H=os.path.dirname(__file__)
if subprocess.call([sys.executable,os.path.join(H,"preflight.py")]): sys.exit("Corrige el preflight antes de generar.")
D=os.path.join(H,"..","design"); L=lambda n:json.load(open(os.path.join(D,n+".json"),encoding="utf-8"))
pal={r["id"]:r["block"] for r in L("palette")}; c=L("city")[0]
o=["package gg.losbloques;","// GENERADO por tools/gen.py desde design/*.json. No editar a mano.","final class CityData {",
"  record Building(String id,int w,int d,int floors,int fh,String wall,String roof,int weight){}",
"  static final Building[] BUILDINGS={"]
for b in L("buildings"):
    o.append(f'    new Building("{b["id"]}",{b["width"]},{b["depth"]},{b["floors"]},{b["floorHeight"]},"{pal[b["wall"]]}","{pal[b["roof"]]}",{b["weight"]}),')
o.append("  };")
for k,v in (("BLOCK_SIZE",c["blockSize"]),("STREET",c["streetWidth"]),("SIDEWALK",c["sidewalk"]),("LAMP_EVERY",c["lampEvery"]),("DEFAULT_BLOCKS",c["defaultBlocks"]),("MAX_BLOCKS",c["maxBlocks"])):
    o.append(f"  static final int {k}={v};")
o.append(f"  static final long SEED={c['seed']}L;")
for k,f in (("ASPHALT","streetBlock"),("SIDEWALK_B","sidewalkBlock"),("LAMP_POST","lampPostBlock"),("LAMP","lampBlock"),("WINDOW","windowBlock")):
    o.append(f'  static final String {k}="{pal[c[f]]}";')
o.append("}")
open(os.path.join(H,"..","src/main/java/gg/losbloques/CityData.java"),"w").write("\n".join(o)+"\n")
print("CityData.java generado")

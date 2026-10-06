import json,sys,os
D=os.path.join(os.path.dirname(__file__),"..","design")
L=lambda n:json.load(open(os.path.join(D,n+".json"),encoding="utf-8"))
pal={r["id"]:r for r in L("palette")}; bld=L("buildings"); city=L("city")[0]
bad=[]
for name,rows in (("palette",list(pal.values())),("buildings",bld),("city",[city])):
    for r in rows:
        for k,v in r.items():
            if v in (None,"",[]): bad.append(f"{name}.{r.get('id')}.{k}: celda vacia")
lot=city["blockSize"]-2*city["sidewalk"]
for k in ("streetBlock","sidewalkBlock","lampPostBlock","lampBlock","windowBlock"):
    if city[k] not in pal: bad.append(f"city.{k} -> '{city[k]}' no existe en palette")
for b in bld:
    for k in ("wall","roof"):
        if b[k] not in pal: bad.append(f"buildings.{b['id']}.{k} -> '{b[k]}' no existe en palette")
    if b["width"]>lot or b["depth"]>lot: bad.append(f"buildings.{b['id']}: no cabe en el solar ({lot})")
    if b["weight"]<=0 or b["floors"]<=0: bad.append(f"buildings.{b['id']}: valores no positivos")
print("PREFLIGHT LIMPIO" if not bad else "\n".join(bad)); sys.exit(1 if bad else 0)

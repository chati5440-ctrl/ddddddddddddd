package gg.losbloques;
// GENERADO por tools/gen.py desde design/*.json. No editar a mano.
final class CityData {
  record Building(String id,int w,int d,int floors,int fh,String wall,String roof,int weight){}
  static final Building[] BUILDINGS={
    new Building("casa_baja",10,10,1,4,"minecraft:white_terracotta","minecraft:smooth_stone",4),
    new Building("tienda",14,12,2,4,"minecraft:bricks","minecraft:polished_deepslate",3),
    new Building("edificio_medio",16,16,4,4,"minecraft:light_gray_concrete","minecraft:polished_deepslate",2),
    new Building("torre",14,14,8,4,"minecraft:smooth_sandstone","minecraft:smooth_stone",1),
  };
  static final int BLOCK_SIZE=24;
  static final int STREET=8;
  static final int SIDEWALK=2;
  static final int LAMP_EVERY=8;
  static final int DEFAULT_BLOCKS=4;
  static final int MAX_BLOCKS=8;
  static final long SEED=1992L;
  static final String ASPHALT="minecraft:black_concrete";
  static final String SIDEWALK_B="minecraft:smooth_stone";
  static final String LAMP_POST="minecraft:iron_bars";
  static final String LAMP="minecraft:sea_lantern";
  static final String WINDOW="minecraft:glass";
}

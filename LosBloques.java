package gg.losbloques;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import java.util.Random;

/** Comando /ciudad [manzanas]: construye una ciudad de bloques alrededor del jugador (usar en mundo plano). */
public class LosBloques implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) ->
            dispatcher.register(CommandManager.literal("ciudad")
                .requires(s -> s.hasPermissionLevel(2))
                .executes(c -> build(c.getSource(), CityData.DEFAULT_BLOCKS))
                .then(CommandManager.argument("manzanas", IntegerArgumentType.integer(1, CityData.MAX_BLOCKS))
                    .executes(c -> build(c.getSource(), IntegerArgumentType.getInteger(c, "manzanas"))))));
    }

    private static BlockState st(String id) {
        Block b = Registries.BLOCK.get(Identifier.tryParse(id));
        return b == null ? Blocks.STONE.getDefaultState() : b.getDefaultState();
    }

    private static void put(ServerWorld w, int x, int y, int z, BlockState s) {
        w.setBlockState(new BlockPos(x, y, z), s, Block.NOTIFY_LISTENERS);
    }

    private static int build(ServerCommandSource src, int n) {
        ServerWorld w = src.getWorld();
        BlockPos o = BlockPos.ofFloored(src.getPosition());
        int gy = o.getY() - 1, cell = CityData.BLOCK_SIZE + CityData.STREET;
        int span = n * cell + CityData.STREET;
        int x0 = o.getX() - span / 2, z0 = o.getZ() - span / 2;
        BlockState asphalt = st(CityData.ASPHALT), walk = st(CityData.SIDEWALK_B);
        for (int x = 0; x < span; x++) for (int z = 0; z < span; z++) put(w, x0 + x, gy, z0 + z, asphalt);
        int total = 0; for (var b : CityData.BUILDINGS) total += b.weight();
        Random rnd = new Random(CityData.SEED);
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
            int bx = x0 + CityData.STREET + i * cell, bz = z0 + CityData.STREET + j * cell;
            for (int x = 0; x < CityData.BLOCK_SIZE; x++) for (int z = 0; z < CityData.BLOCK_SIZE; z++) put(w, bx + x, gy, bz + z, walk);
            lamps(w, bx, bz, gy);
            int r = rnd.nextInt(total); var pick = CityData.BUILDINGS[0];
            for (var b : CityData.BUILDINGS) { if (r < b.weight()) { pick = b; break; } r -= b.weight(); }
            building(w, pick, bx + (CityData.BLOCK_SIZE - pick.w()) / 2, bz + (CityData.BLOCK_SIZE - pick.d()) / 2, gy + 1);
        }
        src.sendFeedback(() -> Text.literal("Ciudad creada: " + n + "x" + n + " manzanas."), true);
        return 1;
    }

    private static void lamps(ServerWorld w, int bx, int bz, int gy) {
        BlockState post = st(CityData.LAMP_POST), lamp = st(CityData.LAMP);
        int e = CityData.BLOCK_SIZE - 1;
        for (int k = 0; k <= e; k += CityData.LAMP_EVERY) for (int[] p : new int[][]{{k,0},{k,e},{0,k},{e,k}}) {
            put(w, bx + p[0], gy + 1, bz + p[1], post); put(w, bx + p[0], gy + 2, bz + p[1], post);
            put(w, bx + p[0], gy + 3, bz + p[1], lamp);
        }
    }

    private static void building(ServerWorld w, CityData.Building b, int x0, int z0, int y0) {
        BlockState wall = st(b.wall()), roof = st(b.roof()), glass = st(CityData.WINDOW), air = Blocks.AIR.getDefaultState();
        int h = b.floors() * b.fh();
        for (int y = 0; y < h; y++) for (int x = 0; x < b.w(); x++) for (int z = 0; z < b.d(); z++) {
            boolean edge = x == 0 || z == 0 || x == b.w() - 1 || z == b.d() - 1;
            BlockState s = air;
            if (edge) {
                int fy = y % b.fh();
                boolean win = (fy == 1 || fy == 2) && ((x + z) % 3 == 1) && !(x == 0 && z == 0);
                s = win ? glass : wall;
            } else if (y % b.fh() == 0 && y > 0) s = roof; // suelo de cada planta
            put(w, x0 + x, y0 + y, z0 + z, s);
        }
        for (int x = 0; x < b.w(); x++) for (int z = 0; z < b.d(); z++) put(w, x0 + x, y0 + h, z0 + z, roof);
        int dx = x0 + b.w() / 2; // puerta en la fachada sur-oeste (z minimo)
        for (int y = 0; y < 2; y++) { put(w, dx, y0 + y, z0, air); put(w, dx + 1, y0 + y, z0, air); }
    }
}

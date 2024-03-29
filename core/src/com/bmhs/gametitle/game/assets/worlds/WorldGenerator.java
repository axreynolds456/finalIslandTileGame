package com.bmhs.gametitle.game.assets.worlds;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bmhs.gametitle.gfx.assets.tiles.statictiles.WorldTile;
import com.bmhs.gametitle.gfx.utils.TileHandler;

import java.util.Arrays;

import static com.badlogic.gdx.math.MathUtils.random;


public class WorldGenerator {

    private int worldMapRows, worldMapColumns;

    private int[][] worldIntMap;

    private int seedColor,grass, savana, dirt, mountain, sand, ocean, highMountain;

    public WorldGenerator (int worldMapRows, int worldMapColumns) {
        this.worldMapRows = worldMapRows;
        this.worldMapColumns = worldMapColumns;

        worldIntMap = new int[worldMapRows][worldMapColumns];

        //call methods to build 2D array

        seedColor = 29;
        grass = 57;
        savana = 31;
        dirt = 49;
        mountain = 9;
        highMountain = 11;
        sand = 29;
        ocean = 19;

        Vector2 mapSeed = new Vector2(random(worldIntMap[0].length), random(worldIntMap.length));
        System.out.println(mapSeed.y + "" + mapSeed.x);

        worldIntMap[(int)mapSeed.y][(int)mapSeed.x] = 4;

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                Vector2 tempVector = new Vector2(c,r);
                if(tempVector.dst(mapSeed) < 10) {
                    worldIntMap[r][c] = seedColor;
                }
            }
        }

        //randomize();
        seedMap(4);
        water();
        seedIslands( (int)(Math.random() * 4 + 4) );
        setBoat(random(10,30) + 5);
        setTrees(random(4,45) + 20);

        /*
        searchAndExpand(MathUtils.random(8,9), seedColor, 39, 0.10);
        searchAndExpand(MathUtils.random(6,7), seedColor, 31, 0.20);
        searchAndExpand(MathUtils.random(4,5), seedColor, 49, 0.30);
        searchAndExpand(MathUtils.random(2,3), seedColor, 62, 0.40);
        searchAndExpand(MathUtils.random(1,2), seedColor, 78, 0.50);
        */

        generateWorldTextFile();

        Gdx.app.error("WorldGenerator", "WorldGenerator(WorldTile[][][])");
    }

    public String getWorld3DArrayToString() {
        String returnString = "";

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                returnString += worldIntMap[r][c] + " ";
            }
            returnString += "\n";
        }

        return returnString;
    }

    public void water(){
        for (int[] ints : worldIntMap) {
            Arrays.fill(ints, ocean);
        }
    }

    public void seedMap(int num) {
        for(int i = 0; i < num; i++){
            Vector2 mapSeed = new Vector2(random(worldIntMap[0].length), random(worldIntMap.length));
            for(int r = 0; r < worldIntMap.length; r++) {
                for(int c = 0; c < worldIntMap[r].length; c++) {
                    Vector2 tempVector = new Vector2(c,r);
                    if(tempVector.dst(mapSeed) < 10) {
                        worldIntMap[r][c] = 56;
                    }
                }
            }
        }
    }

    private void seedIslands(int num) {
        for(int i = 0; i < num; i++) {
            int rSeed = random(worldIntMap.length-1);
            int cSeed = random(worldIntMap[0].length-1);
            worldIntMap[rSeed][cSeed] = seedColor;
            randomIslandExpansion(rSeed, cSeed);
        }
    }

    //Goal:
    // Make islands of different sizes, not just a circle or a square
    // Grow seed in any direction and get different results
    private void randomIslandExpansion(int row, int column) {
        int expansionRadius = random(2,5);

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                if (worldIntMap[r][c] == seedColor) {
                    for (int i = 0; i < expansionRadius; i++) {
                        int directions = random(2,4);
                        for (int j = 0; j < directions; j++){
                            int direction = random(0,8);
                            switch (direction) {
                                case 0:
                                    row--;
                                    setGrass(row - 5, column);
                                    setSavannah(row - 4, column);
                                    setDirt(row - 3, column);
                                    setMountain(row - 2, column);
                                    setHighMountain(row - 1, column);
                                    setSand(row - 6, column);
                                    expandIsland(row -1, column);
                                    break;
                                case 1:
                                    column++;
                                    setGrass(row, column + 5);
                                    setSavannah(row, column + 4);
                                    setDirt(row, column + 3);
                                    setMountain(row, column + 2);
                                    setHighMountain(row, column + 1);
                                    setSand(row, column + 6);
                                    expandIsland(row, column + 1);
                                    break;
                                case 2:
                                    row++;
                                    setGrass(row + 5, column);
                                    setSavannah(row + 4, column);
                                    setDirt(row + 3, column);
                                    setMountain(row + 2, column);
                                    setHighMountain(row + 1, column);
                                    setSand(row + 6, column);
                                    expandIsland(row + 1, column);
                                    break;
                                case 3:
                                    column--;
                                    setGrass(row, column - 5);
                                    setSavannah(row, column - 4);
                                    setDirt(row, column - 3);
                                    setMountain(row, column - 2);
                                    setHighMountain(row, column - 1);
                                    setSand(row, column - 6);
                                    expandIsland(row, column - 1);
                                    break;
                                case 4:
                                    row--;
                                    column--;
                                    setGrass(row - 5, column - 5);
                                    setSavannah(row - 4, column - 4);
                                    setDirt(row - 3, column - 3);
                                    setMountain(row - 2, column - 2);
                                    setHighMountain(row - 1, column - 1);
                                    setSand(row - 6, column - 6);
                                    expandIsland(row - 1, column - 1);
                                    break;
                                case 5:
                                    row--;
                                    column++;
                                    setGrass(row - 5, column + 5);
                                    setSavannah(row - 4, column + 4);
                                    setDirt(row - 3, column + 3);
                                    setMountain(row - 2, column + 2);
                                    setHighMountain(row - 1, column + 1);
                                    setSand(row - 6, column + 6);
                                    expandIsland(row - 1, column + 1);
                                    break;
                                case 6:
                                    row++;
                                    column--;
                                    setGrass(row + 5, column - 5);
                                    setSavannah(row + 4, column - 4);
                                    setDirt(row + 3, column - 3);
                                    setMountain(row + 2, column - 2);
                                    setHighMountain(row + 1, column - 1);
                                    expandIsland(row + 1, column - 1);
                                    break;
                                case 7:
                                    row++;
                                    column++;
                                    setGrass(row + 5, column + 5);
                                    setSavannah(row + 4, column + 4);
                                    setDirt(row + 3, column + 3);
                                    setMountain(row + 2, column + 2);
                                    setHighMountain(row + 1, column + 1);
                                    setSand(row + 6, column + 6);
                                    expandIsland(row + 1, column + 1);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void setGrass(int centerRow, int centerCol) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {
                    if(worldIntMap[r][c] != seedColor){
                        worldIntMap[r][c] = grass;
                    }
                }
            }
        }
    }

    private void setSavannah(int centerRow, int centerCol) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {
                    if (worldIntMap[r][c] != seedColor && worldIntMap[r][c] != grass) {
                        worldIntMap[r][c] = savana;
                    }
                }
            }
        }
    }

    private void setDirt(int centerRow, int centerCol) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {
                    if (worldIntMap[r][c] != seedColor && worldIntMap[r][c] != grass && worldIntMap[r][c] != savana) {
                        worldIntMap[r][c] = dirt;
                    }
                }
            }
        }
    }

    private void setMountain(int centerRow, int centerCol) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {
                    if (worldIntMap[r][c] != seedColor && worldIntMap[r][c] != grass && worldIntMap[r][c] != savana && worldIntMap[r][c] != dirt) {
                        worldIntMap[r][c] = mountain;
                    }
                }
            }
        }
    }

    private void setHighMountain(int centerRow, int centerCol) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {
                    if (worldIntMap[r][c] != grass && worldIntMap[r][c] != savana && worldIntMap[r][c] != dirt && worldIntMap[r][c] != mountain) {
                        worldIntMap[r][c] = highMountain;
                    }
                }
            }
        }
    }

    private void setSand(int centerRow, int centerCol) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {
                    if (worldIntMap[r][c] != seedColor && worldIntMap[r][c] != grass && worldIntMap[r][c] != savana && worldIntMap[r][c] != dirt && worldIntMap[r][c] != mountain && worldIntMap[r][c] != highMountain) {
                        worldIntMap[r][c] = sand;
                    }
                }
            }
        }
    }

    private void setTrees(int numTrees) {
        double randChance = random();
        for(int i = 0; i < numTrees; i++) {
            int r = random(worldIntMap.length-1);
            int c = random(worldIntMap[0].length-1);
            if(randChance > .50) {
                if(worldIntMap[r][c] != seedColor && worldIntMap[r][c] != dirt && worldIntMap[r][c] != mountain && worldIntMap[r][c] != highMountain && worldIntMap[r][c] != sand && worldIntMap[r][c] != ocean) {
                    tree(r,c);
                }
            }
        }
    }

    private void tree (int row, int col) {
        worldIntMap[row][col] = 145;
        worldIntMap[row][col+1] = 146;
        worldIntMap[row][col+2] = 147;
        worldIntMap[row-1][col] = 150;
        worldIntMap[row-1][col+1] = 151;
        worldIntMap[row-1][col+2] = 152;
    }



    private void setBoat(int numBoats) {
        double randChance = random();
        for(int i = 0; i < numBoats; i++) {
            int r = random(worldIntMap.length-1);
            int c = random(worldIntMap[0].length-1);
            if(randChance > .50) {
                if(worldIntMap[r][c] != seedColor && worldIntMap[r][c] != grass && worldIntMap[r][c] != savana && worldIntMap[r][c] != dirt && worldIntMap[r][c] != mountain && worldIntMap[r][c] != highMountain && worldIntMap[r][c] != sand) {
                    worldIntMap[r][c] = 137;
                    worldIntMap[r][c+1] = 138;
                }
            }
            if(worldIntMap[r][c] != seedColor && worldIntMap[r][c] != grass && worldIntMap[r][c] != savana && worldIntMap[r][c] != dirt && worldIntMap[r][c] != mountain && worldIntMap[r][c] != highMountain && worldIntMap[r][c] != sand) {
                worldIntMap[r][c] = 139;
                worldIntMap[r][c+1] = 140;
            }
        }
    }

    private void expandIsland(int row, int column) {
        if (row >= 0 && row < worldIntMap.length && column >= 0 && column < worldIntMap[row].length && worldIntMap[row][column] != seedColor) {
            worldIntMap[row][column] = grass;
        }
    }
    private void searchAndExpand(int radius, int numToFind, int numToWrite, double probability) {
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                if(worldIntMap[r][c] == numToFind) {
                    for(int subRow = r-radius; subRow <= r+radius; subRow++) {
                        for(int subCol = c-radius; subCol <= c+radius; subCol++) {
                            if(subRow >= 0 && subCol >= 0 && subRow <= worldIntMap.length-1 && subCol <= worldIntMap[0].length-1 && worldIntMap[subRow][subCol] != numToFind) {
                                if(Math.random() > probability) {
                                    worldIntMap[subRow][subCol] = numToWrite;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void randomize() {
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                worldIntMap[r][c] = random(TileHandler.getTileHandler().getWorldTileArray().size-1);
            }
        }
    }

    public WorldTile[][] generateWorld() {
        WorldTile[][] worldTileMap = new WorldTile[worldMapRows][worldMapColumns];
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                worldTileMap[r][c] = TileHandler.getTileHandler().getWorldTileArray().get(worldIntMap[r][c]);
            }
        }
        return worldTileMap;
    }

    private void generateWorldTextFile() {
        FileHandle file = Gdx.files.local("assets/worlds/world.text");
        file.writeString(getWorld3DArrayToString(), false);
    }

}
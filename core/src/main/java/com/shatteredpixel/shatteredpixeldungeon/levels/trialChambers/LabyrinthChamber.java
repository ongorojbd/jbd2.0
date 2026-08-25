package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Neoro;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Maze;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

import java.util.ArrayDeque;
import java.util.Arrays;

//inspired by the vanilla SecretMazeRoom: the whole room is carved with the same maze algorithm
//(levels.features.Maze) instead of a hand-placed layout, and the reward sits at whichever dead
//end ends up farthest (by path length, via a local BFS) from the north door. No monsters -
//the maze itself, and whatever roams in from the other rooms while you're navigating it, is
//the obstacle.
public class LabyrinthChamber extends TrialChamber {

    {
        isBuildWithStructure = false;
    }

    private int prizeCell;

    @Override
    public void build() {
        super.build();

        int w = innerWidth, h = innerHeight;
        boolean[][] maze = new boolean[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (x == 0 || x == w - 1 || y == 0 || y == h - 1) {
                    maze[x][y] = Maze.FILLED;
                }
            }
        }
        //keep the 4 door tiles open so the maze algorithm (which never touches the border
        //cells it's given as walls) can't seal off a way in or out
        maze[topDoor.x - innerRoom.left][topDoor.y - innerRoom.top] = Maze.EMPTY;
        maze[bottomDoor.x - innerRoom.left][bottomDoor.y - innerRoom.top] = Maze.EMPTY;
        maze[leftDoor.x - innerRoom.left][leftDoor.y - innerRoom.top] = Maze.EMPTY;
        maze[rightDoor.x - innerRoom.left][rightDoor.y - innerRoom.top] = Maze.EMPTY;

        Maze.allowDiagonals = false;
        maze = Maze.generate(maze);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (maze[x][y] == Maze.FILLED) {
                    Painter.set(level, innerRoom.left + x, innerRoom.top + y, Terrain.WALL);
                }
            }
        }

        Point deepest = findDeepestCell(maze, topDoor.x - innerRoom.left, topDoor.y - innerRoom.top);
        prizeCell = level.pointToCell(new Point(innerRoom.left + deepest.x, innerRoom.top + deepest.y));
        Painter.set(level, prizeCell, Terrain.PEDESTAL);
    }

    //BFS over the generated maze to find the empty cell with the longest path from (startX,startY)
    private Point findDeepestCell(boolean[][] maze, int startX, int startY) {
        int w = maze.length, h = maze[0].length;
        int[][] dist = new int[w][h];
        for (int[] row : dist) Arrays.fill(row, -1);

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        dist[startX][startY] = 0;
        queue.add(new int[]{startX, startY});

        int bestX = startX, bestY = startY, bestDist = 0;
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int i = 0; i < 4; i++) {
                int nx = cur[0] + dx[i], ny = cur[1] + dy[i];
                if (nx >= 0 && nx < w && ny >= 0 && ny < h && !maze[nx][ny] && dist[nx][ny] == -1) {
                    dist[nx][ny] = dist[cur[0]][cur[1]] + 1;
                    if (dist[nx][ny] > bestDist) {
                        bestDist = dist[nx][ny];
                        bestX = nx;
                        bestY = ny;
                    }
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        return new Point(bestX, bestY);
    }

    @Override
    public void placeRewards() {
        Heap heap;
        if (bonusReward != null) {
            heap = level.drop(bonusReward, prizeCell);
        } else {
            heap = level.drop(new Neoro(), prizeCell);
        }
        heap.type = Heap.Type.CHEST;
    }
}

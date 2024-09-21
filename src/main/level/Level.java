package main.level;

import main.entity.Entity;
import main.road.Road;

public class Level {
    final private Road road;

    public Level(Road road) {
        this.road = road;
    }

    public void sortLeaderboard(Entity[] cars, int n) {
        if (n < 2) {
            return;
        }

        int mid = n / 2;
        Entity[] l = new Entity[mid];
        Entity[] r = new Entity[n - mid];

        System.arraycopy(cars, 0, l, 0, mid);
        System.arraycopy(cars, mid, r, 0, n - mid);

        sortLeaderboard(l, mid);
        sortLeaderboard(r, n - mid);

        merge(cars, l, r, mid, n - mid);
    }

    private void merge(Entity[] cars, Entity[] l, Entity[] r, int left, int right) {
        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l[i].pos <= r[j].pos) {
                cars[k++] = l[i++];
            } else {
                cars[k++] = r[j++];
            }
        }
        while (i < left) {
            cars[k++] = l[i++];
        }
        while (j < right) {
            cars[k++] = r[j++];
        }
    }
}

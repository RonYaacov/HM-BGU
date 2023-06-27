package com.company;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TileFactory {
    private List<Supplier<Player>> playersList;
    private Map<Character, Supplier<Enemy>> enemiesMap;
    private Player selected;

    public TileFactory(){
        playersList = initPlayers();
        enemiesMap = initEnemies();
    }

    private Map<Character, Supplier<Enemy>> initEnemies() {
        List<Supplier<Enemy>> enemies = Arrays.asList(
                () -> new Monster('s', "Lannister Solider", new Health(80,80), 8, 3,25, 3),
                () -> new Monster('k', "Lannister Knight",  new Health(200,200), 14, 8, 50,   4),
                () -> new Monster('q', "Queen's Guard",  new Health(400,400), 20, 15, 100,  5),
                () -> new Monster('z', "Wright",  new Health(600,600), 30, 15,100, 3),
                () -> new Monster('b', "Bear-Wright",  new Health(1000,1000), 75, 30, 250,  4),
                () -> new Monster('g', "Giant-Wright", new Health(1500,1500), 100, 40,500,   5),
                () -> new Monster('w', "White Walker",  new Health(2000,2000), 150, 50, 1000, 6),
                () -> new Monster('M', "The Mountain",  new Health(1000,100), 60, 25,  500, 6),
                () -> new Monster('C', "Queen Cersei",  new Health(100,100), 10, 10,1000, 1),
                () -> new Monster('K', "Night's King",  new Health(5000,5000), 300, 150, 5000, 8),
                () -> new Trap('B', "Bonus Trap",  new Health(1,1), 1,1,250,1,5),
                () -> new Trap('Q', "Queen's Trap",  new Health(250,250), 50, 10, 100, 3, 7),
                () -> new Trap('D', "Death Trap",  new Health(500,500), 100, 20, 250, 1, 10)
        );

        return enemies.stream().collect(Collectors.toMap(s -> s.get().getTile(), Function.identity()));
    }

    private List<Supplier<Player>> initPlayers() {
        return Arrays.asList(
                () -> new Warrior('@',"Jon Snow", new Health(300,300), 30, 4, 3),
                () -> new Warrior('@',"The Hound", new Health(400,400), 20, 6, 5),
                () -> new Mage('@',"Melisandre", new Health(100,100), 5, 1, 300, 30, 15, 5, 6),
                () -> new Mage('@',"Thoros of Myr", new Health(250,250), 25, 4, 150, 20, 20, 3, 4),
                () -> new Rogue('@',"Arya Stark", new Health(150,150), 40, 2, 20),
                () -> new Rogue('@',"Bronn", new Health(250,250), 35, 3, 50)
        );
    }

    public List<Player> listPlayers(){
        return playersList.stream().map(Supplier::get).collect(Collectors.toList());
    }

    public Tile produceObject(char character) {
        if(character == '#')
            return new Wall();
        if(character == '.')
            return new EmptyTile();
        if (enemiesMap.containsKey(character)) {
            Supplier<Enemy> enemySupplier = enemiesMap.get(character);
            return enemySupplier.get();
        }

        Optional<Supplier<Player>> playerSupplier = playersList.stream()
                .filter(supplier -> supplier.get().getTile() == character)
                .findFirst();

        if (playerSupplier.isPresent()) {
            return playerSupplier.get().get();
        }
        return null;

    }

    public Tile producePlayer(String name){
        Optional<Supplier<Player>> playerSupplier = playersList.stream()
                .filter(supplier -> supplier.get().getName().equals(name))
                .findFirst();

        if (playerSupplier.isPresent()) {
            return playerSupplier.get().get();
        }
        return null;
    }

}

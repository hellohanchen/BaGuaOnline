package com.hellohanchen.baguaserver.repo;

import com.hellohanchen.baguaserver.entity.GameId;
import com.hellohanchen.baguaserver.entity.PlayerId;
import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@EzySingleton
@EzyRepository
@NoArgsConstructor
public class PlayerGameRepo implements EzyDatabaseRepository<PlayerId, GameId> {
    private final HashMap<PlayerId, GameId> games = new HashMap<>();

    @Override
    public long count() {
        return games.size();
    }

    @Override
    public int deleteAll() {
        int size = games.size();
        games.clear();
        return size;
    }

    public boolean tryDelete(PlayerId id) {
        if (games.containsKey(id)) {
            games.remove(id);
            return true;
        }

        return false;
    }

    @Override
    public void delete(PlayerId id) {
        tryDelete(id);
    }

    @Override
    public int deleteByIds(Collection<PlayerId> ids) {
        AtomicInteger counter = new AtomicInteger();

        ids.forEach(i -> {
            if (tryDelete(i)) {
                counter.incrementAndGet();
            }
        });

        return counter.get();
    }

    @Override
    public List<GameId> findAll() {
        return games.values().stream().toList();
    }

    @Override
    public List<GameId> findAll(int skip, int limit) {
        return findAll().stream().skip(skip).limit(limit).toList();
    }

    @Override
    public GameId findByField(String s, Object o) {
        return null;
    }

    @Override
    public GameId findById(PlayerId playerId) {
        return games.get(playerId);
    }

    @Override
    public List<GameId> findListByField(String s, Object o) {
        return null;
    }

    @Override
    public List<GameId> findListByField(String s, Object o, int i, int i1) {
        return null;
    }

    @Override
    public List<GameId> findListByIds(Collection<PlayerId> collection) {
        return collection.stream().map(this::findById).toList();
    }

    @Override
    public void save(Iterable<GameId> iterable) {
        iterable.forEach(this::save);
    }

    @Override
    public void save(GameId gameId) {
        // NOOP
    }

    public boolean add(PlayerId player, GameId game) {
        if (games.containsKey(player)) {
            return false;
        }

        games.put(player, game);
        return true;
    }
}

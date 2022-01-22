package com.hellohanchen.baguaserver.repo;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.baguaserver.entity.GameId;
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
public class GameRepo implements EzyDatabaseRepository<GameId, GameManager> {
    private final HashMap<GameId, GameManager> games = new HashMap<>();

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

    public boolean tryDelete(GameId id) {
        if (games.containsKey(id)) {
            games.remove(id);
            return true;
        }

        return false;
    }

    @Override
    public void delete(GameId id) {
        tryDelete(id);
    }

    @Override
    public int deleteByIds(Collection<GameId> ids) {
        AtomicInteger counter = new AtomicInteger();

        ids.forEach(i -> {
            if (tryDelete(i)) {
                counter.incrementAndGet();
            }
        });

        return counter.get();
    }

    @Override
    public List<GameManager> findAll() {
        return games.values().stream().toList();
    }

    @Override
    public List<GameManager> findAll(int skip, int limit) {
        return games.values().stream().skip(skip).limit(limit).toList();
    }

    @Override
    public GameManager findByField(String s, Object o) {
        return null;
    }

    @Override
    public GameManager findById(GameId id) {
        return games.get(id);
    }

    @Override
    public List<GameManager> findListByField(String s, Object o) {
        return null;
    }

    @Override
    public List<GameManager> findListByField(String s, Object o, int i, int i1) {
        return null;
    }

    @Override
    public List<GameManager> findListByIds(Collection<GameId> collection) {
        return collection.stream().map(this::findById).toList();
    }

    @Override
    public void save(Iterable<GameManager> iterable) {
        iterable.forEach(this::save);
    }

    @Override
    public void save(GameManager gameManager) {
        games.put(gameManager.getId(), gameManager);
    }
}

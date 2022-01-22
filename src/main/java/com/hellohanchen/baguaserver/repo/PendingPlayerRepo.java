package com.hellohanchen.baguaserver.repo;

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
public class PendingPlayerRepo implements EzyDatabaseRepository<PlayerId, PlayerId> {
    private final HashMap<PlayerId, PlayerId> players = new HashMap<>();

    @Override
    public long count() {
        return players.size();
    }

    @Override
    public int deleteAll() {
        int size = players.size();
        players.clear();
        return size;
    }

    public boolean tryDelete(PlayerId id) {
        if (players.containsKey(id)) {
            players.remove(id);
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
    public List<PlayerId> findAll() {
        return players.values().stream().toList();
    }

    @Override
    public List<PlayerId> findAll(int skip, int limit) {
        return findAll().stream().skip(skip).limit(limit).toList();
    }

    @Override
    public PlayerId findByField(String s, Object o) {
        return null;
    }

    @Override
    public PlayerId findById(PlayerId playerId) {
        return players.get(playerId);
    }

    @Override
    public List<PlayerId> findListByField(String s, Object o) {
        return null;
    }

    @Override
    public List<PlayerId> findListByField(String s, Object o, int i, int i1) {
        return null;
    }

    @Override
    public List<PlayerId> findListByIds(Collection<PlayerId> collection) {
        return collection.stream().map(this::findById).toList();
    }

    @Override
    public void save(Iterable<PlayerId> iterable) {
        iterable.forEach(this::save);
    }

    @Override
    public void save(PlayerId player) {
        players.put(player, player);
    }
}

package com.hellohanchen.baguaserver.repo;

import com.hellohanchen.baguaserver.entity.PlayerCredential;
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
public class CredentialRepo implements EzyDatabaseRepository<PlayerId, PlayerCredential> {
    private final HashMap<PlayerId, PlayerCredential> credentials = new HashMap<>();

    @Override
    public long count() {
        return credentials.size();
    }

    @Override
    public int deleteAll() {
        int size = credentials.size();
        credentials.clear();
        return size;
    }

    public boolean tryDelete(PlayerId id) {
        if (credentials.containsKey(id)) {
            credentials.remove(id);
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
    public List<PlayerCredential> findAll() {
        return credentials.values().stream().toList();
    }

    @Override
    public List<PlayerCredential> findAll(int skip, int limit) {
        return findAll().stream().skip(skip).limit(limit).toList();
    }

    @Override
    public PlayerCredential findByField(String s, Object o) {
        return null;
    }

    @Override
    public PlayerCredential findById(PlayerId playerId) {
        return credentials.get(playerId);
    }

    @Override
    public List<PlayerCredential> findListByField(String s, Object o) {
        return null;
    }

    @Override
    public List<PlayerCredential> findListByField(String s, Object o, int i, int i1) {
        return null;
    }

    @Override
    public List<PlayerCredential> findListByIds(Collection<PlayerId> collection) {
        return collection.stream().map(this::findById).toList();
    }

    @Override
    public void save(Iterable<PlayerCredential> iterable) {
        iterable.forEach(this::save);
    }

    @Override
    public void save(PlayerCredential playerCredential) {
        credentials.put(playerCredential.id(), playerCredential);
    }
}

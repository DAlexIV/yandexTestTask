package com.yandex.perfstorage.di;

import com.yandex.perfstorage.db.PerformersProvider;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by dalexiv on 8/11/16.
 */

@Singleton
@Component(modules = {NetModule.class})
public interface ProviderComponent {
    void inject(PerformersProvider provider);
}

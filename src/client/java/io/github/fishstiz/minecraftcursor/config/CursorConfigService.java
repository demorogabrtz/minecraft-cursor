package io.github.fishstiz.minecraftcursor.config;

import io.github.fishstiz.minecraftcursor.cursor.Cursor;

public class CursorConfigService {
    private final CursorConfigLoader loader;

    public CursorConfigService(String path) {
        assert path.endsWith(CursorConfigLoader.FILE_EXTENSION)
                : "File not supported. Must be: " + CursorConfigLoader.FILE_EXTENSION;

        this.loader = new CursorConfigLoader(path);
    }

    public void saveSettings(Cursor... cursors) {
        saveSettings(false, cursors);
    }

    public void saveSettings(boolean force, Cursor... cursors) {
        boolean hasApplied = false;

        for (Cursor cursor : cursors) {
            CursorConfig.Settings settings = CursorConfig.Settings.create(cursor.getScale(), cursor.getXhot(), cursor.getYhot(), cursor.getEnabled());
            if (hasChanges(loader.config().getOrCreateCursorSettings(cursor.getType()), settings)) {
                this.loader.config().updateCursorSettings(cursor.getType(), settings);
                hasApplied = true;
            }
        }

        if (hasApplied || force) {
            this.loader.save();
        }
    }

    public CursorConfig get() {
        return this.loader.config();
    }

    public static boolean hasChanges(CursorConfig.Settings oldSettings, CursorConfig.Settings newSettings) {
        boolean isChanged = false;

        isChanged |= oldSettings.getEnabled() != newSettings.getEnabled();
        isChanged |= oldSettings.getScale() != newSettings.getScale();
        isChanged |= oldSettings.getXHot() != newSettings.getXHot();
        isChanged |= oldSettings.getYHot() != newSettings.getYHot();

        return isChanged;
    }
}

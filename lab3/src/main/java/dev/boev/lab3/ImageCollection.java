package dev.boev.lab3;

import java.io.File;
import java.io.FilenameFilter;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.HashMap;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class ImageCollection implements Aggregate {
    private File[] files;
    // Кэш EXIF-данных: путь файла → информация
    private final Map<String, Map<String, String>> exifCache = new HashMap<>();

    public ImageCollection(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            this.files = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jpg") ||
                            name.toLowerCase().endsWith(".jpeg");
                }
            });
            if (this.files != null) {
                java.util.Arrays.sort(this.files);
            }
        } else {
            this.files = new File[0];
        }
    }

    public ImageCollection(File[] files) {
        this.files = files != null ? files : new File[0];
    }

    @Override
    public Iterator getIterator() {
        return new ImageFileIterator();
    }

    public File getFile(int index) {
        if (files == null || files.length == 0) return null;
        if (index < 0) index = files.length - 1;
        else if (index >= files.length) index = 0;
        return files[index];
    }

    // Получение EXIF с кэшированием
    public Map<String, String> getExifInfo(File file) {
        if (file == null) return new HashMap<>();
        String path = file.getAbsolutePath();

        if (exifCache.containsKey(path)) {
            return exifCache.get(path);
        }

        Map<String, String> info = new HashMap<>();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (ifd0 != null) {
                if (ifd0.containsTag(ExifIFD0Directory.TAG_MAKE))
                    info.put("Камера", ifd0.getString(ExifIFD0Directory.TAG_MAKE));
                if (ifd0.containsTag(ExifIFD0Directory.TAG_MODEL))
                    info.put("Модель", ifd0.getString(ExifIFD0Directory.TAG_MODEL));
            }

            ExifSubIFDDirectory subIfd = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (subIfd != null) {
                if (subIfd.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL))
                    info.put("Дата съёмки", subIfd.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
                if (subIfd.containsTag(ExifSubIFDDirectory.TAG_EXPOSURE_TIME))
                    info.put("Выдержка", subIfd.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME) + "с");
                if (subIfd.containsTag(ExifSubIFDDirectory.TAG_FNUMBER))
                    info.put("Диафрагма", "f/" + subIfd.getString(ExifSubIFDDirectory.TAG_FNUMBER));
                if (subIfd.containsTag(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT))
                    info.put("ISO", subIfd.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
            }
        } catch (Exception e) {
            info.put("EXIF", "Недоступны");
        }
        info.put("Файл", file.getName());
        info.put("Размер", formatSize(file.length()));

        exifCache.put(path, info);
        return info;
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " Б";
        if (bytes < 1024 * 1024) return String.format("%.1f КБ", bytes / 1024.0);
        return String.format("%.1f МБ", bytes / (1024.0 * 1024));
    }

    public int size() { return files != null ? files.length : 0; }
    public boolean isEmpty() { return files == null || files.length == 0; }

    // Для анимации: получить индекс следующего/предыдущего
    public int getNextIndex(int current) {
        return files != null && files.length > 0 ? (current + 1) % files.length : -1;
    }
    public int getPrevIndex(int current) {
        return files != null && files.length > 0 ? (current - 1 + files.length) % files.length : -1;
    }

    class ImageFileIterator implements Iterator {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return files != null && files.length > 0;
        }

        @Override
        public Object next() {
            if (!hasNext()) throw new NoSuchElementException("Collection is empty");
            File current = files[currentIndex];
            currentIndex = (currentIndex + 1) % files.length;
            return current;
        }

        @Override
        public Object preview() {
            if (!hasNext()) throw new NoSuchElementException("Collection is empty");
            return files[currentIndex]; // не двигаем индекс
        }

        @Override
        public boolean hasPreview() {  // Исправлено
            return files != null && files.length > 0;
        }

        @Override
        public int getCurrentIndex() {
            return currentIndex;
        }

        public void previous() {
            if (!hasNext()) throw new NoSuchElementException("Collection is empty");
            currentIndex = (currentIndex - 1 + files.length) % files.length;
        }
        public void reset() { currentIndex = 0; }
        public File getCurrentFile() {
            if (!hasNext()) return null;
            return files[currentIndex];
        }
    }
}
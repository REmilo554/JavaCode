import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MyStringBuilder {
    private char[] text;
    private int count;
    private List<Snapshot> historyOfSnapshots = new ArrayList<Snapshot>();

    public MyStringBuilder() {
        this(16);
    }

    public MyStringBuilder(int capacity) {
        text = new char[capacity];
        count = 0;
        saveState();
    }

    public MyStringBuilder(String str) {
        this(str.length() + 16);
        append(str);
    }

    private void saveState() {
        historyOfSnapshots.add(createSnapshot());
    }

    public MyStringBuilder append(String str) {
        if (str == null) {
            str = "null";
        }
        int len = str.length();
        ensureCapacity(count + len);
        str.getChars(0, len, text, count);
        count += len;
        saveState();
        return this;
    }

    private void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity > text.length) {
            expandCapacity(minimumCapacity);
        }
    }

    private void expandCapacity(int minimumCapacity) {
        int newCapacity = Math.max(text.length * 2, minimumCapacity);
        text = Arrays.copyOf(text, newCapacity);
    }

    public MyStringBuilder deleteChar(int index) {
        if ((index < 0) || (index >= count)) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        System.arraycopy(text, index + 1, text, index, count - index - 1);
        count--;
        saveState();
        return this;
    }

    public void undo() {
        if (historyOfSnapshots.size() > 1) {
            historyOfSnapshots.removeLast(); // Remove last action
            Snapshot snapshot = historyOfSnapshots.getLast();
            restoreState(snapshot);
            System.out.println("Отмена выполнена. Текущее состояние: " + this.toString());
        } else {
            System.out.println("Ничего нельзя отменять.");
        }
    }

    private Snapshot createSnapshot() {
        return new Snapshot(Arrays.copyOf(text, text.length), count);
    }

    private void restoreState(Snapshot snapshot) {
        this.text = snapshot.getValue();
        this.count = snapshot.getCount();
    }

    @Override
    public String toString() {
        return new String(text, 0, count);
    }

    @AllArgsConstructor
    @Getter
    private class Snapshot {
        private final char[] value;
        private final int count;
    }
}

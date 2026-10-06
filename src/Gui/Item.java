package Gui;

/** Elemento de un JComboBox: muestra "id - texto" pero conserva el id internamente. */
record Item(int id, String texto) {
    @Override
    public String toString() {
        return id + " - " + texto;
    }
}

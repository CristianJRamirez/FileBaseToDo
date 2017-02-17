package a45858000w.filebasetodo;

/**
 * Created by 45858000w on 17/02/17.
 */

public class Imagen {
    /**
     * Clase que representa la existencia de un Imagen
     */

        private String nombre;
        private int idDrawable;

        public Imagen(String nombre, int idDrawable) {
            this.nombre = nombre;
            this.idDrawable = idDrawable;
        }

        public String getNombre() {
            return nombre;
        }

        public int getIdDrawable() {
            return idDrawable;
        }

        public int getId() {
            return nombre.hashCode();
        }

        public static Imagen[] ITEMS = null;


        /**
         * Obtiene item basado en su identificador
         *
         * @param id identificador
         * @return Imagen
         */
        public static Imagen getItem(int id) {
            for (Imagen item : ITEMS) {
                if (item.getId() == id) {
                    return item;
                }
            }
            return null;
        }

}

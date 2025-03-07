package festivales.modelo;

import java.util.*;


/**
 * Esta clase guarda una agenda con los festivales programados
 * en una serie de meses
 *
 * La agenda guardalos festivales en una colecci�n map
 * La clave del map es el mes (un enumerado festivales.modelo.festivales.modelo.Mes)
 * Cada mes tiene asociados en una colecci�n ArrayList
 * los festivales  de ese mes
 *
 * Solo aparecen los meses que incluyen alg�n festival
 *
 * Las claves se recuperan en orden alfab�ico
 *
 */
public class AgendaFestivales {
    private TreeMap<Mes, ArrayList<Festival>> agenda;

    public AgendaFestivales() {
        this.agenda = new TreeMap<>();
    }

    /**
     * a�ade un nuevo festival a la agenda
     * <p>
     * Si la clave (el mes en el que se celebra el festival)
     * no existe en la agenda se crear� una nueva entrada
     * con dicha clave y la colecci�n formada por ese �nico festival
     * <p>
     * Si la clave (el mes) ya existe se a�ade el nuevo festival
     * a la lista de festivales que ya existe ese ms
     * insert�ndolo de forma que quede ordenado por nombre de festival.
     * Para este segundo caso usa el m�todo de ayuda
     * obtenerPosicionDeInsercion()
     */
    public void addFestival(Festival festival) {
        if (!agenda.containsKey(festival.getFechaInicio())) {
            ArrayList<Festival> festivales = new ArrayList<>();
            festivales.add(festival);
            agenda.put(festival.getMes(), festivales);
        } else {
            agenda.get(festival.getMes()).add(festival);
        }


    }

    /**
     * @param festivales una lista de festivales
     * @param festival
     * @return la posici�n en la que deber�a ir el nuevo festival
     * de forma que la lista quedase ordenada por nombre
     */
    private int obtenerPosicionDeInsercion(ArrayList<Festival> festivales, Festival festival) {
        int posicion = 0;
        for (int i = 0; i < festivales.size(); i++) {
            if (festival.getNombre().compareTo(festivales.get(i).getNombre()) > 0) {
                posicion++;
            }
        }
        return posicion;

    }

    /**
     * Representaci�n textual del festival
     * De forma eficiente
     * Usa el conjunto de entradas par recorrer el map
     */
    @Override
    public String toString() {
        Set<Map.Entry<Mes, ArrayList<Festival>>> ent = agenda.entrySet();
        Iterator<Map.Entry<Mes, ArrayList<Festival>>> it = ent.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("Festivales\n");
        while(it.hasNext()){
            Map.Entry<Mes, ArrayList<Festival>> entrada = it.next();
            sb.append("\n" + entrada.getKey() + " ("+ festivalesEnMes(entrada.getKey()) +
                    " festival/es)");
            for(Festival fest:entrada.getValue()){
                sb.append(fest);
            }
        }
        return sb.toString();
    }

    /**
     * @param mes el mes a considerar
     * @return la cantidad de festivales que hay en ese mes
     * Si el mes no existe se devuelve -1
     */
    public int festivalesEnMes(Mes mes) {

        return agenda.get(mes).size();
    }

    /**
     * Se trata de agrupar todos los festivales de la agenda
     * por estilo.
     * Cada estilo que aparece en la agenda tiene asociada una colecci�n
     * que es el conjunto de nombres de festivales que pertenecen a ese estilo
     * Importa el orden de los nombres en el conjunto
     * <p>
     * Identifica el tipo exacto del valor de retorno
     */
    public TreeMap festivalesPorEstilo() {
        TreeMap<Estilo, ArrayList<Festival>> agendaEstilos = new TreeMap<>();
        Mes[] meses = Mes.values();
        for (int i = 0; i < meses.length; i++) {
            ArrayList<Festival> festivales = agenda.get(meses[i]);
            for (Festival fest : festivales) {
                HashSet<Estilo> estilos = fest.getEstilos();
                for (Estilo est : estilos) {

                    if (agendaEstilos.containsKey(est)) {
                        agendaEstilos.get(est).add(fest);
                    } else {
                        ArrayList<Festival> festivals = new ArrayList<>();
                        festivals.add(fest);
                        agendaEstilos.put(est, festivals);
                    }

                }
            }

        }

        return agendaEstilos;
    }


    /**
     * Se cancelan todos los festivales organizados en alguno de los
     * lugares que indica el conjunto en el mes indicado. Los festivales
     * concluidos o que no empezados no se tienen en cuenta
     * Hay que borrarlos de la agenda
     * Si el mes no existe se devuelve -1
     * <p>
     * Si al borrar de un mes los festivales el mes queda con 0 festivales
     * se borra la entrada completa del map
     */
    public int festivalesPorEstilo(HashSet<String> lugares, Mes mes) {
        Mes[] meses = Mes.values();
        ArrayList<Festival> festivales = agenda.get(mes);
        for(Festival fest:festivales){
            if(lugares.contains(fest.getLugar())){
                agenda.get(mes).remove(fest);
            }
        }
        if(festivales.size() == 0){
            agenda.remove(mes);
        }
        for(int i = 0; i < meses.length; i++){
            if(meses[i].equals(String.valueOf(mes))){
                return i + 1;
            }
        }
        return -1;


    }
}



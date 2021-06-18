/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;

public class Maze {

    final static char C = ' ', X = 'x', S = 's', E = 'e', V = '.';

    final static int START_I = 0, START_J = 6;
    final static int END_I = 9, END_J = 8;

    /* S = Salida, E = entrada, sino viceversa, X = Muro, C = camino */
    private static final char[][] laberinto = { {X, X, X, X, X, X, E, X, X, X},
                                                {X, C, X, C, C, C, C , C, C, X},
                                                {X, C, C, C, X, C, C , X, C, X},
                                                {X, X, X, X, C, C, X , X, X, X},
                                                {X, C, C, C, C, X, C , C, C, X},
                                                {X, C, X, X, X, X, C , X, C, X},
                                                {X, C, C, C, C, X, C , X, C, X},
                                                {X, X, C, X, X, X, C , X, C, X},
                                                {X, C, C, C, C, C, C , X, C, X},
                                                {X, X, X, X, X, X, X , X, S, X}};

    public int tamLaberinto() {
        return laberinto.length;
    }

    public void imprimir() {
        for (int i = 0; i < tamLaberinto(); i++) {
            for (int j = 0; j < tamLaberinto(); j++) {
                System.out.print(laberinto[i][j]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public char marcar(int i, int j, char valor) {
        assert (isInMaze(i, j));
        char tmp = laberinto[i][j];
        laberinto[i][j] = valor;
        return tmp;
    }

    public char marcar(MazePos pos, char value) {
        return marcar(pos.i(), pos.j(), value);
    }

    public boolean isMarked(int i, int j) {
        assert (isInMaze(i, j));
        return (laberinto[i][j] == V);
    }

    public boolean isMarked(MazePos pos) {
        return isMarked(pos.i(), pos.j());
    }

    public boolean isClear(int i, int j) {
        assert (isInMaze(i, j));
        return (laberinto[i][j] != X && laberinto[i][j] != V);
    }

    public boolean isClear(MazePos pos) {
        return isClear(pos.i(), pos.j());
    }

    //true if cell is within maze 
    public boolean isInMaze(int i, int j) {
        if (i >= 0 && i < tamLaberinto() && j >= 0 && j < tamLaberinto()) {
            return true;
        } else {
            return false;
        }
    }

    //true if cell is within maze 
    public boolean isInMaze(MazePos pos) {
        return isInMaze(pos.i(), pos.j());
    }

    public boolean isFinal(int i, int j) {
        return (i == Maze.END_I && j == Maze.END_J);
    }

    public boolean isFinal(MazePos pos) {
        return isFinal(pos.i(), pos.j());
    }

    @Override
    public char[][] clone() {

        char[][] mazeCopy = new char[tamLaberinto()][tamLaberinto()];
        for (int i = 0; i < tamLaberinto(); i++) {
            System.arraycopy(laberinto[i], 0, mazeCopy[i], 0, tamLaberinto());
        }
        return mazeCopy;
    }

    public void restaurar(char[][] savedMaze) {
        for (int i = 0; i < tamLaberinto(); i++) {
            for (int j = 0; j < tamLaberinto(); j++) {
                laberinto[i][j] = savedMaze[i][j];
            }
        }
    }

    public static void main(String[] args) {

        Maze maze = new Maze();
        maze.imprimir();

        System.out.println("\n\nEncuentra una ruta con una pila: ");
        maze.resolverConPila();

        System.out.println("\n\nEncuentra una ruta con una cola: ");
        maze.resolverConCola();

        System.out.println("\n\nEncuentra una ruta recursiva: ");
        maze.resolverRecursion();

    }

    //El objetivo es encontrar un camino de principio a fin 
    //**************************************************
    //Esta solución utiliza una pila para realizar un seguimiento de la posible 
    //estados / posiciones para explorar; que marca el laberinto para recordar la
    // posiciones que ya ha explorado.
    public void resolverConPila() {

        //guardar el laberinto
        char[][] savedMaze = clone();

        //declaran las ubicaciones de pila
        Stack<MazePos> candidatos = new Stack<>();

        //inserte el inicio
        candidatos.push(new MazePos(START_I, START_J));

        MazePos crt, next;
        while (!candidatos.empty()) {

            //obtener la posición actual
            crt = candidatos.pop();

            if (isFinal(crt)) {
                break;
            }

            //marcar la posición actual
            marcar(crt, V);

            //poner sus vecinos en la cola
            next = crt.irNorte();
            if (isInMaze(next) && isClear(next)) {
                candidatos.push(next);
            }
            next = crt.IrEste();
            if (isInMaze(next) && isClear(next)) {
                candidatos.push(next);
            }
            next = crt.IrOeste();
            if (isInMaze(next) && isClear(next)) {
                candidatos.push(next);
            }
            next = crt.irSur();
            if (isInMaze(next) && isClear(next)) {
                candidatos.push(next);
            }
        }

        if (!candidatos.empty()) {
            System.out.println("Ya lo tienes!");
        } else {
            System.out.println("Estás atrapado en el laberinto!");
        }
        imprimir();

        //restaurar el laberinto
        restaurar(savedMaze);
    }

//    ************************************************** 
//     esta solución utiliza una cola para realizar un seguimiento de la posible 
//     estados / posiciones para explorar; que marca el laberinto para recordar la 
//     posiciones que ya ha explorado.
    public void resolverConCola() {

        //guardar el laberinto
        char[][] savedMaze = clone();

        //declaran las ubicaciones de pila 
        LinkedList<MazePos> candidates = new LinkedList<>();

        //inserte el inicio 
        candidates.add(new MazePos(START_I, START_J));

        MazePos crt, next;
        while (!candidates.isEmpty()) {

            //obtener la posición actual
            crt = candidates.removeFirst();

            if (isFinal(crt)) {
                break;
            }

            //marcar la posición actual
            marcar(crt, V);

            //poner sus vecinos en la cola
            next = crt.irNorte();
            if (isInMaze(next) && isClear(next)) {
                candidates.add(next);
            }
            next = crt.IrEste();
            if (isInMaze(next) && isClear(next)) {
                candidates.add(next);
            }
            next = crt.IrOeste();
            if (isInMaze(next) && isClear(next)) {
                candidates.add(next);
            }
            next = crt.irSur();
            if (isInMaze(next) && isClear(next)) {
                candidates.add(next);
            }
        }

        if (!candidates.isEmpty()) {
            System.out.println("Ya lo tienes!");
        } else {
            System.out.println("Estás atrapado en el laberinto!");
        }
        imprimir();

        //restaurar el laberinto
        restaurar(savedMaze);
    }

//    ************************************************** resolver utilizando 
//     recursividad. Nota: esta solución desmarca la ruta de acceso al alcanzar 
//     callejones sin salida, así que al final se queda sólo el camino marcado. es 
//     posible escribir una solución que no no un-Marcar una de sus huellas, 
//     pero en cambio, se clona y restaura el laberinto.
    public void resolverRecursion() {

        if (solve(new MazePos(START_I, START_J))) {
            System.out.println("lo tengo: ");
        } else {
            System.out.println("Estás atrapado en el laberinto.");
        }
        imprimir();

    }

//    encontrar un camino para salir del laberinto de esta posición. obras 
//     recursiva, avanzando a un vecino y continuando desde 
//     allí. Si se encuentra una ruta, devolverá true. De lo contrario, devuelve false.
    public boolean solve(MazePos pos) {

        //base case
        if (!isInMaze(pos)) {
            return false;
        }
        if (isFinal(pos)) {
            return true;
        }
        if (!isClear(pos)) {
            return false;
        }

        //posición actual debe ser clara
        assert (isClear(pos));

//        recurso 
//        primera Marcar esta ubicación
        marcar(pos, V);

        //tratar de ir hacia el sur
        if (solve(pos.irSur())) {
//            encontramos una solución que va al sur: si queremos salir de la 
//             laberinto limpio, y luego desmarcar celda actual y volver; si nos 
//             querer Marcar el camino en el laberinto, entonces no desmarcar 
//             marca (pos, C);
            return true;
        }

        //más al oeste 
        if (solve(pos.IrOeste())) {
//            encontramos una solución que va al oeste: si queremos salir de la 
//             laberinto limpio, y luego desmarcar celda actual y volver; si nos 
//             querer Marcar el camino en el laberinto, entonces no desmarcar 
//             retorno 
//             marca (pos, C);
            return true;
        }

        //else irNorte 
        if (solve(pos.irNorte())) {
//            encontramos una solución que va al norte: si queremos salir de la 
//             laberinto limpio, y luego desmarcar celda actual y volver; si nos 
//             querer Marcar el camino en el laberinto, entonces no desmarcar 
//             retorno 
//              Marcar una (pos, C); 
            return true;
        }

        //más al este
        if (solve(pos.IrEste())) {
//           encontramos una solución que va al este: si queremos salir de la 
//             laberinto limpio, y luego desmarcar celda actual y volver; si nos 
//             querer Marcar el camino en el laberinto, entonces no desmarcar 
//             retorno 
//             marca (pos, C);
            return true;
        }

//        desmarcar todos los callejones sin salida; ya que estuvo marcada, la posición debe 
//         han sido claros
        marcar(pos, C);

        //if none of the above returned, then there is no solution
        return false;
    }
};

class MazePos {

    int i, j;

    public MazePos(int i, int j) {
        this.i = i;
        this.j = j;
    }

    ;
    public int i() {
        return i;
    }

    public int j() {
        return j;
    }

    public void print() {
        System.out.println("(" + i + "," + j + ")");
    }

    public MazePos irNorte() {
        return new MazePos(i - 1, j);
    }

    public MazePos irSur() {
        return new MazePos(i + 1, j);
    }

    public MazePos IrEste() {
        return new MazePos(i, j + 1);
    }

    public MazePos IrOeste() {
        return new MazePos(i, j - 1);
    }

};
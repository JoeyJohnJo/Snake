package framework;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
/*Componente que pode ser adicionado em alguma outra classe pra receber eventos
* de tecla mais facilmente*/
public class KeyHandler implements KeyListener {

    private ArrayList<Integer> pressedKeys; //Lista de teclas apertadas
    private Component component; //O componente em que se deve ouvir os eventos de tecla
    //No pacote AWT, apenas classes do tipo component pode receber eventos de mouse, tecla, etc.

    //Instancia tudo
    public KeyHandler(Component c) {
        pressedKeys = new ArrayList<>();
        component = c;
        component.addKeyListener(this); //Adiciona esse listener no componente, nesse caso, o canvas
    }

    public ArrayList<Integer> getPressedKeys() {
        return pressedKeys;
    } //Retorna a lista de teclas apertadas

    //Nao é usado aqui mas precisamos implementar por causa da interface KeyListener, ent só deixa em branco
    @Override
    public void keyTyped(KeyEvent e) {
        //Se quiser fazer alguma coisa pra esse metodo, só escrever aqui
    }

    //O que acontece quando uma tecla é pressionada
    @Override
    public void keyPressed(KeyEvent e) {
        //Se nao estiver na lista de teclas pressionadas, adicona ela pra lista
        if (!pressedKeys.contains(e.getKeyCode())) pressedKeys.add( e.getKeyCode());
        System.out.println(pressedKeys); //Para questoes de debugging
    }

    //O que acontece quando uma tecla é solta
    @Override
    public void keyReleased(KeyEvent e) {
        //Remove ela da lista de teclas pressionadas
        pressedKeys.remove((Integer) e.getKeyCode());
    }
}

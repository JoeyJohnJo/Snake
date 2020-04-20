package framework.states;

import java.awt.*;
import java.util.ArrayList;

/*Gerenciador de estados, manipula as telas do jogo,
* as ligacoes entre as telas, qual tela está ativa agora,
* qual tela estava ativada antes, etc.
* Ver proximo: State.java*/
public class StateMachine {
    private ArrayList<State> states; // Lista de todos os estados gerenciados
    private State currentState; // O estado ativo
    private State previousState; // O ultimo estado ativo
    private Component component; // A janela em que os estados devem ser atualizados
    //Somente objetos do tipo componente podem ter listeners pra teclado, mouse, etc. por isso precisamos de um

    //Instancia tudo. null == nada
    public StateMachine(Component component){
        this.component = component;
        states = new ArrayList<>();
        currentState = null;
        previousState = null;
    }

    //Adiciona um novo estado pra ser gerenciado
    public void addState(State state) {
        this.states.add(state);
    }

    public State getCurrentState() {return currentState;}  //Retorna o estado ativo no momento
    public State getPreviousState() {return previousState;} //Retorna o estado ativo antes do atual
    //Usado pra mudar de estado
    public void setCurrentState(State currentState) {
        previousState = this.currentState; // O atual se torna o anterior
        this.currentState = currentState; // O estado do parametro se torna o atual
    }
    public Component getComponent() {return component;} // Retorna o canvas do jogo, que é o componente
}

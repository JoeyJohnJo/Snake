package framework.states;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

//Define tudo que um estado precisa ter pra ser valido
//Ver Proximo: TitleScreen.java, ScoreState.java, PlayingState.java
public abstract class State {
    protected String name; //Nome, pra questoes de debugging
    protected StateMachine stateMachine; //StateMachine pra gerenciar o estado atual e dar acesso ao canvas se necessario
    protected State nextState; //Mais um estado, pra formar ligaçao com ele de sequencia. É a tela que vem depois desta no jogo

    protected Font titleFont; //Essas fontes só estão aqui porque é a unica que é usada nesse jogo em específico, e são usadas pra tudo
    protected Font subFont; // Não é tecnicamente necessário

    //Inicializa o estado com um nome e um gerenciador, pra caso não há um proximo estado definido ainda
    public State(String name, StateMachine stateMachine) {
        this.name = name;
        this.stateMachine = stateMachine;
        this.stateMachine.addState(this); //Adiciona esse estado na lista de estados gerenciados
        //Usando fonte customizada
        registerFont();
        subFont = titleFont.deriveFont(16f);
    }

    //Todos os estados precisam fazer algo para renderizar e atualizar os dados
    public abstract void render(Graphics g);
    public abstract void tick();
    //Não tem implementaçao nessa classe porque a implementaçao depende
    // dos estados específicos, nao sao generalizadas

    @Override
    public String toString() {
        return name;
    } //Se quiser saber o nome do estaado

    public void setNextState(State nextState) {
        this.nextState = nextState;
    } //Define o proximo estadp

    //Carrega o proximo estado, o proximo estado se torna o estado ativo na tela
    protected void loadNextState() {
        stateMachine.setCurrentState(nextState);
    }

    //Registrar a fonte personalizada
    private void registerFont() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        InputStream file = getClass().getResourceAsStream("/framework/fonts/bit5x5.ttf");
        try {
            titleFont = Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(48f);
            ge.registerFont(titleFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}

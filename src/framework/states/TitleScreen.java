package framework.states;

import framework.KeyHandler;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import static framework.Frame.SCALED_WIDTH;

//Tela incial que se ve quando começa o aplicativo
public class TitleScreen extends State {
    private String start = "PRESS SPACE TO START"; //A string que aparece no meio da tela
    private int frames; // Usado pra fazer a string piscar
    private KeyHandler keyHandler; //Listener pra saber se apertou espaço ou nao

    //Instancia a classe
    public TitleScreen(StateMachine stateMachine) {
        super("Title Screen", stateMachine);
        setNextState(new PlayingState("Playing State", stateMachine)); //DEfine o próximo estado como o PlayingState
        keyHandler = new KeyHandler(stateMachine.getComponent()); // Inicializa o listener de teclas
    }

    //Como vai ser renderizado esse estado na tela
    //Implementando metodo render do State.java que estava sem implementação
    //Lembrando que tick e render rodam 60 vezes por segundo
    @Override
    public void render(Graphics g) {
        g.setFont(titleFont); //Define a fonte (titleFont vem da classe State)
        g.setColor(Color.WHITE); //Define a cor
        //Escreve snake em cima da tela
        g.drawString("SNAKE", SCALED_WIDTH/2 - g.getFontMetrics(titleFont).stringWidth("SNAKE") /2, 60);
        g.setFont(subFont); //muda o tamanho pra 16
        //Escreve a mensagem que aparece no meio da tela
        g.drawString(start, SCALED_WIDTH/2 - g.getFontMetrics(subFont).stringWidth(start) /2, 250);
    }

    //Como os valores desse estado vao ser atualizados
    //Implementando metodo tick do State.java que estava sem implementação
    //Lembrando que tick e render rodam 60 vezes por segundo
    @Override
    public void tick() {
        //Esses ifs servem só pra piscar a mensagem na tela
        frames++;
        if (frames > 30) {
            start = "PRESS SPACE TO START";
        }
        if (frames > 80) {
            frames = 0;
            start = "";
        }

        //Se o espaço for apertado
        if (keyHandler.getPressedKeys().contains(KeyEvent.VK_SPACE)) {
            System.out.println("Space pressed"); //Para debugging
            stateMachine.getComponent().removeKeyListener(keyHandler); //Tira esse listener
            //Precisamos tirar esse listener porque vamos adicionar o listener da cobra quando carregar a proxima tela
            //Tecnicamente se n tirar n muda nada, mas nao é bom deixar dois listeners
            loadNextState(); //Carregar a proxima tela, no caso o PlayingState
        }
    }
}

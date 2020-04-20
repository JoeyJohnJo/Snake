package framework.entities;

import framework.Frame;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;
//Normalmente esta classe daria extends numa classe entity, mas como é um jogo pequeno é desnecessario
//A comida nao precisa atualizar nada ent nao precisa de metodo tick
public class Food{
    private int x, y; // posicao da comida
    private int width = 10; //largura
    private int height = 10; //altura

    public Food() {
        //Pega dois numeros aleatorios pra ser a posicao da comida
        int randX = new Random().nextInt(Frame.SCALED_WIDTH - width); // O maximo por x sendo a largura da tela
        int randY = new Random().nextInt(Frame.SCALED_HEIGHT - height); // o maximo pro y sendo a altura da tela

        //Checa se os numeros aleatorios n coincidem com a posicao de algum bloco da cobra
        for (Block b : Snake.getBlocks()) {
            //Se coincidir com alguma parte da cobra, pega um novo numero aleatorio
            if (randX > b.x && randX < b.x + b.getWidth() && randY > b.y && randY < b.y + b.getHeight()) {
                randX = new Random().nextInt(Frame.SCALED_WIDTH - width);
                randY = new Random().nextInt(Frame.SCALED_HEIGHT - height);
            }
        }

        /*Tecnicamente esse check não é perfeito, precisaria colocar el
        * em algum loop pra ver se o novo numero aleatorio também não coincide com algum bloco do corpo
        * mas no geral isso é improvavel*/

        //Define o x e y nessa nova posicao aleatoria
        x = randX;
        y = randY;
    }

    //Renderiza a comida
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height); //Só um quadrado vermelho na posicao x e y
    }

    //Diz se foi comido ou nao
    public boolean isCollected(Snake head) {
        Rectangle self = new Rectangle();
        self.setBounds(x, y, width, height); //Faz um retangulo com os dados da comida
        Rectangle snake = new Rectangle();
        snake.setBounds(head.getHead().x, head.getHead().y, head.getHead().getWidth(), head.getHead().getHeight());
        //Faz um retangulo com os dados da cabeça da cobra

        return self.intersects(snake); //Se os dois retangulos se cruzarem retorna verdadeiro, se nao, retorna falso
    }
}

package framework.entities;

import framework.Frame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
//normalmente esta classe daria extends numa classe entity, mas como é um jogo pequeno é desnecessario
//Block é o que constitui a cobra
public class Block {
    //poderiam ser uma variavel so ja que trata a tela como uma grade e se a velocidade for menor ou maior, nao funcionaria
    private int width =  10; //largura
    private int height = 10; //altura
    private int speed =  10; //velocidade
    public int x, y; //posicao

    private int numRows = Frame.SCALED_HEIGHT / height; //numero de linhas, tratando a tela como grade
    private int numColumns = Frame.SCALED_WIDTH / width ; //numero de colunas, tratando a tela como grade
    private int rowHeight = Frame.SCALED_HEIGHT / numRows; //altura da linha
    private int columnWidth = Frame.SCALED_WIDTH /numColumns; //largura da coluna
    private int currentRow, currentColumn; //linha e coluna atual

    //Posicao passada, pro proximo bloco na cobra saber pra qual posicao precisa ir
    private ArrayList<Integer> previousPos  = new ArrayList<>();

    //Inicializa um bloco em uma posicao aleatoria, só é usado pro bloco da cabeça
    public Block() {
        x = new Random().nextInt(Frame.SCALED_WIDTH);
        y = new Random().nextInt(Frame.SCALED_HEIGHT);
        currentRow = x / columnWidth; //Define a linha atual baseada nessa posicao aleatoria
        currentColumn = y / rowHeight; //Define a coluna atual baseada nessa posicao aleatoria

        //Arredonda o x e y pra baixo, pq como a tela é tratada como uma grade
        //o x nao pode ser 52 por exemplo, tem que alinhar a grade ent ele deve ser 50 ou 60
        x = ((x+5)/10)*10;
        y = ((y+5)/10)*10;
    }
    /*Usado pra aumentar o tamanho da cobra, inicializa o novo bloco atras do bloco b no parametro
    * basead em qual direçao a cobra está indo*/
    public Block(Block b, int i) {
        switch(i) {
            case KeyEvent.VK_DOWN:
                x = b.x;
                y = b.y - height;
                break;
            case KeyEvent.VK_UP:
                x = b.x;
                y = b.y + height;
                break;
            case KeyEvent.VK_LEFT:
                y = b.y;
                x = b.x + width;
                break;
            case KeyEvent.VK_RIGHT:
                y = b.y;
                x = b.x - width;
                break;
        }
        currentRow = x / columnWidth; //Define a linha atual baseada nessa posicao aleatoria
        currentColumn = y / rowHeight; //Define a coluna atual baseada nessa posicao aleatoria

        //Arredonda o x e y pra baixo, pq como a tela é tratada como uma grade
        //o x nao pode ser 52 por exemplo, tem que alinhar a grade ent ele deve ser 50 ou 60
        x = ((x+5)/10)*10;
        y = ((y+5)/10)*10;
    }
    //Renderiza o bloco
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height); //Só um quadrado branco na posicao x e y
    }

    //Atualiza a posicao
    public void updatePos() {
        previousPos.clear(); //Limpa a posicao passada
        previousPos.add(currentRow) ;//Adiciona a posicao atual como se fosse a passada
        previousPos.add(currentColumn); //Adiciona a posicao atual como se fosse a passada
        //Atualiza a linha e coluna atual baaseado na nova posicao x e y
        currentRow = x / columnWidth;
        currentColumn = y / rowHeight;
    }
    /*Retorna a posicao anterior como dois valores x e y,
    * usado na cobra pra colocar o bloco que estiver atras deste
    * na posicao anterior que este estava, assim da o efeito dos blocos estarem seguindo o mesmo caminho que a cabeca*/
    public ArrayList<Integer> getPreviousPos() {
        return this.previousPos;
    }

    /////////////////////GETS E SETS//////////////////////////////
    public void setRow(int row) {
        int difference = currentRow - row;
        if (difference > 0) {
            for (int i = 0; i < difference; i++)
                x -= speed;
        }
        else if (difference < 0) {
            for (int i = Math.abs(difference); i > 0; i--)
                x+=speed;
        }

        x = ((x+5)/10)*10;
    }
    public void setColumn(int column) {
        int difference = currentColumn - column;
        if (difference > 0) {
            for (int i = 0; i < difference; i++)
                y -= speed;
        }
        else if (difference < 0) {
            for (int i = Math.abs(difference); i > 0; i--)
                y += speed;
        }
        y = ((y+5)/10)*10;
    }
    public int getSpeed() {
        return speed;
    }

    public boolean isColliding(Block b) {
        Rectangle bRect = new Rectangle();
        bRect.setBounds(b.x, b.y, b.width, b.height);
        Rectangle self = new Rectangle();
        self.setBounds(x, y, width, height);
        return self.intersects(bRect);
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    ////////////////////////////////////////////////////////////////////
}

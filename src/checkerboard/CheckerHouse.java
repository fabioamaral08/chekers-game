package checkerboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.EventListener;

import javax.swing.JPanel;



/**
 * Casa do tabuleiro de damas.<br>
 * Para manipular eventos implemente uma interface {@link EventListener}.
 * @author Rafael Silva Santos
 *
 */
public class CheckerHouse extends JPanel{
	/**
	 * Especificca que a casa é vazia.<br>
	 * Não contém nenhuma pedra.
	 */
	public static final int CONTENT_TYPE_EMPTY = 0;
	/**
	 * Especifica que casa contém um peão.
	 */
	public static final int CONTENT_TYPE_MEN = 1;
	/**
	 * Especifica que a casa contém uma dama.
	 */
	public static final int CONTENT_TYPE_KING= 2;

	/**
	 * Especifica que a casa não está selecionada.
	 */
	public static final int SELECTION_MODE_NONE = 0;
	/**
	 * Especifica que a casa está selecionada.
	 */
	public static final int SELECTION_MODE_SELECTED = 1;
	/**
	 * Especifica que a casa é um posição destino.
	 * A casa pode ser alcançada pelo movimento da pedra, seja peão ou dama, a partir da casa selecionada.
	 */
	public static final int SELECTION_MODE_MOVE = 2;


	private float margin;

	private Color bgColor;
	private Color fgColor;
	private Color selectionColor;

	private int contentType;
	private int selectionMode;
	private BasicStroke selectionBorder;
	
	private int row;
	private int col;

	
	/**
	 * Cria uma casa de tabuleiro.
	 * Esta casa pode ser vazia, conter uma pedra comum ou dama.
	 * Em relação à seleção, pode ou não estar selecionada e ainda ser uma casa possível a partir do movimento de uma pedra.
	 * Por padrão, a casa é vazia e não selecionada.
	 * @param row Posição em relação à linha. <b>Advertência:</b> Não utilizado internamente. Não apresenta qualquer influencia nessa classe.
	 * @param col Posição em relação à coluna. <b>Advertência:</b> Não utilizado internamente. Não apresenta qualquer influencia nessa classe.
	 */
	public CheckerHouse(int row, int col) {
		margin = 9;
		contentType = CheckerHouse.CONTENT_TYPE_EMPTY;
		selectionMode = CheckerHouse.SELECTION_MODE_NONE;

		bgColor = Color.WHITE;
		fgColor = new Color(0, 0, 200);

		selectionBorder = new BasicStroke(margin / 2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		selectionColor = new Color(0, 200, 0);
		
		this.row = row;
		this.col = col;

		setLayout(null);
	}



	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;		
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	//Habilita Antialising


		g2D.setBackground(bgColor);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		
		
		if(selectionMode == CheckerHouse.SELECTION_MODE_SELECTED) {
			g2D.setColor(selectionColor);
			drawSelectionRect(g2D);
		}
		else if(selectionMode == CheckerHouse.SELECTION_MODE_MOVE) {
			g2D.setColor(selectionColor.brighter());
			drawSelectionRect(g2D);
		}	

		if(contentType == CheckerHouse.CONTENT_TYPE_MEN) {
			fillMenPiece(g2D);
		}
		else if(contentType == CheckerHouse.CONTENT_TYPE_KING) {
			fillKingPiece(g2D);
		}	
	}

	
	/**
	 * Calcula o raio de uma pedra, seja peão ou dama.
	 * @return Raio da pedra.
	 */
	protected float calcRadius() {
		return  (getWidth() - 2.0f * margin) / 2.0f;
	}


	/**
	 * Desenha uma pedra to tipo peão. Para alterar a cor da pedra veja utilize o método {@link #setFgColor(Color)}.
	 * Somente invocado se o opção {@link #CONTENT_TYPE_MEN} estiver definida.
	 * O espaçamento entre a borda da casa e a pedra desenhada pode ser alterada por meio do método {@link #setMargin(float)}. 
	 * @param g2D Contexto gráfico do componente.
	 */
	protected void fillMenPiece(Graphics2D g2D) {
		float radius = calcRadius(); 
		float diameter = 1.25f * radius;

		g2D.setColor(fgColor.darker());
		Ellipse2D.Float circle0 = new Ellipse2D.Float(1.4f * margin, 1.4f * margin, 2.0f * radius, 2.0f * radius);
		g2D.fill(circle0);

		g2D.setColor(fgColor);
		Ellipse2D.Float circle1 = new Ellipse2D.Float(margin, margin, 2.0f * radius, 2.0f * radius);
		g2D.fill(circle1);

		g2D.setColor(fgColor.brighter());
		Ellipse2D.Float circle2 = new Ellipse2D.Float((getWidth() - diameter) / 2.0f, (getWidth() - diameter) / 2.0f, diameter, diameter);
		g2D.fill(circle2);
	}



	/**
	 * Desenha o contorno de um retângulo de seleção por toda a casa. Para alterar o tipo de borda utilize o método {@link #setSelectionBorder(BasicStroke)}. 
	 * Somente invocado se  o opção {@link #SELECTION_MODE_SELECTED} ou  o opção {@link #SELECTION_MODE_MOVE} estiver definida.
	 * A cor do contorno depende do tipo de seleção. 
	 * @param g2D Contexto gráfico do componente.
	 */
	protected void drawSelectionRect(Graphics2D g2D) {
		Rectangle2D rect = new Rectangle2D.Float(selectionBorder.getLineWidth() / 2.0f, selectionBorder.getLineWidth() / 2.0f, getWidth() - selectionBorder.getLineWidth(), getHeight() - selectionBorder.getLineWidth());

		g2D.setStroke(selectionBorder);

		g2D.draw(rect);
	}



	/**
	 * Desenha uma pedra to tipo dama. Para alterar a cor da pedra veja utilize o método {@link #setFgColor(Color)}.
	 * Somente invocado se o opção {@link #CONTENT_TYPE_KING} estiver definida.
	 * O espaçamento entre a borda da casa e a pedra desenhada pode ser alterada por meio do método {@link #setMargin(float)}. 
	 * @param g2D Contexto gráfico do componente.
	 */
	protected void fillKingPiece(Graphics2D g2D) {
		float radius = calcRadius(); 
		float diameter = 1.25f * radius;

		g2D.setColor(fgColor.darker());
		Ellipse2D.Float circle0 = new Ellipse2D.Float(1.4f * margin, 1.4f * margin, 2.0f * radius, 2.0f * radius);
		g2D.fill(circle0);

		g2D.setColor(fgColor);
		Ellipse2D.Float circle1 = new Ellipse2D.Float(margin, margin, 2.0f * radius, 2.0f * radius);
		g2D.fill(circle1);

		g2D.setColor(fgColor.brighter());
		Ellipse2D.Float circle2 = new Ellipse2D.Float((getWidth() - diameter) / 2.0f, (getWidth() - diameter) / 2.0f, diameter, diameter);
		g2D.fill(circle2);
		

		g2D.setColor(fgColor.darker());
		Ellipse2D.Float circle4 = new Ellipse2D.Float(circle0.x - 0.4f * margin - 2, circle0.y - 0.4f * margin - 2, circle0.width, circle0.height);
		g2D.fill(circle4);

		g2D.setColor(fgColor);
		Ellipse2D.Float circle5 = new Ellipse2D.Float(circle1.x - 0.4f * margin - 2, circle1.y - 0.4f * margin - 2, circle1.width, circle1.height);
		g2D.fill(circle5);

		g2D.setColor(fgColor.brighter());
		Ellipse2D.Float circle6 = new Ellipse2D.Float(circle2.x - 0.4f * margin - 2, circle2.y - 0.4f * margin - 2, circle2.width, circle2.height);
		g2D.fill(circle6);
	}


	/**
	 * Retorna o tipo de conteúdo da casa. Entre os possíveis conteúdos estão: nenhum, peão ou dama.
	 * @return Tipo de conteúdo da casa. Valores que podem ser assumidos: {@value #CONTENT_TYPE_EMPTY}, {@value #CONTENT_TYPE_MEN} e {@value #CONTENT_TYPE_KING}. 
	 */
	public int getContentType() {
		return contentType;
	}



	public void setContentType(int contentType) {
		this.contentType = contentType;
		repaint();
	}



	public int getSelectionMode() {
		return selectionMode;
	}



	public void setSelectionMode(int selectionMode) {
		this.selectionMode = selectionMode;
		repaint();
	}







	public BasicStroke getSelectionBorder() {
		return selectionBorder;
	}



	public void setSelectionBorder(BasicStroke selectionBorder) {
		this.selectionBorder = selectionBorder;
		repaint();
	}



	public Color getBgColor() {
		return bgColor;
	}


	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
		repaint();
	}



	public float getMargin() {
		return margin;
	}


	public void setMargin(float margin) {
		this.margin = margin;
		repaint();
	}



	public Color getFgColor() {
		return fgColor;
	}



	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
		repaint();
	}



	public Color getSelectionColor() {
		return selectionColor;
	}



	public void setSelectionColor(Color selectionColor) {
		this.selectionColor = selectionColor;
		repaint();
	}



	public int getRow() {
		return row;
	}



	public void setRow(int row) {
		this.row = row;
	}



	public int getCol() {
		return col;
	}



	public void setCol(int col) {
		this.col = col;
	}

	
	@Override
	public String toString() {
		return "CheckerHouse [contentType=" + contentType + ", row=" + row + ", col=" + col + "]";
	}
}

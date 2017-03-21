/**
 * 
 */
package edu.ilstu.it226.assignment3.kwdalle;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

/**
 * @author kwdalle
 *
 */
public class RectangleLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Point start;

	// Will be used to store the union rectangle.
	private Rectangle union;
	private Point end;

	// Signals when buttons have been pressed to RectangleLabel.
	static int button = 0;
	static int button1 = 0;
	static int button2 = 0;
	static int button3 = 0;
	static int button4 = 0;
	final static float dash1[] = { 10.0f };

	// Stores Rectangles for drawing later.
	private static ArrayList<Rectangle> recs = new ArrayList<Rectangle>();
	private static ArrayList<Rectangle> inter = new ArrayList<Rectangle>();

	public RectangleLabel() {
		// Instantiates the object by creating a listener and adding it.
		RectangleMouseListener listener = new RectangleMouseListener();
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// Override for the paintComponent so Rectangles can be painted to the
		// screen.
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		// Here it paints all rectangles in the array. It then draws all
		// intersections. Then the union, and lastly the common area.
		for (Rectangle r : recs) {
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.GREEN);
			g2.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
			g2.setColor(Color.BLACK);
			g2.drawRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
			for (int k = 0; k < recs.size(); k++) {
				for (int i = 0; i < recs.size(); i++) {
					if (i != k) {
						Rectangle temp = recs.get(k).intersect(recs.get(i));
						g2.setColor(Color.GREEN);
						g2.fillRect(temp.getX(), temp.getY(), temp.getWidth(),
								temp.getHeight());
						g2.setColor(Color.BLACK);
						g2.drawRect(temp.getX(), temp.getY(), temp.getWidth(),
								temp.getHeight());

					}
				}
			}
			if (button == 1) {
				for (int k = 0; k < recs.size(); k++) {
					for (int i = 0; i < recs.size(); i++) {
						if (i != k) {
							inter.add(recs.get(k).intersect(recs.get(i)));
						}
					}
				}
				for (Rectangle r1 : inter) {
					g2.setColor(Color.RED);
					g2.fillRect(r1.getX(), r1.getY(), r1.getWidth(),
							r1.getHeight());
					g2.setColor(Color.BLACK);
					g2.drawRect(r1.getX(), r1.getY(), r1.getWidth(),
							r1.getHeight());
				}
				inter.clear();
			}
			if (button1 == 1) {
				if (recs.size() > 1) {
					Rectangle union = recs.get(0);
					for (Rectangle tempR : recs) {
						for (Rectangle tempR1 : recs) {
							Rectangle temp = tempR.union(tempR1);
							union = union.union(temp);
						}
					}
					g2.setColor(Color.BLACK);
					g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_BUTT,
							BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f));
					g2.drawRect(union.getX(), union.getY(), union.getWidth(),
							union.getHeight());
				}
			}
			if (button2 == 1) {
				if (recs.size() == 1) {
					g2.setColor(Color.BLUE);
					g2.fillRect(recs.get(0).getX(), recs.get(0).getY(), recs
							.get(0).getWidth(), recs.get(0).getHeight());
					g2.drawRect(recs.get(0).getX(), recs.get(0).getY(), recs
							.get(0).getWidth(), recs.get(0).getHeight());
				} else {
					Rectangle temp = recs.get(0);
					boolean common = temp.overlaps(recs.get(1));
					for (int i = 0; i < recs.size() && common == true; i++) {
						common = temp.overlaps(recs.get(i));
					}
					if (common) {
						Rectangle intersect = recs.get(0)
								.intersect(recs.get(1));
						for (Rectangle r5 : recs) {
							intersect = intersect.intersect(r5);
						}
						g2.setColor(Color.BLUE);
						g2.fillRect(intersect.getX(), intersect.getY(),
								intersect.getWidth(), intersect.getHeight());
						g2.drawRect(intersect.getX(), intersect.getY(),
								intersect.getWidth(), intersect.getHeight());
					}
				}
			}
		}
		// Here is the logic for when your dragging, it starts with the square
		// and then adds intersections, union, and common area.
		if (start != null && end != null) {
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.GREEN);
			Rectangle r = new Rectangle(start.x, start.y, end.x - start.x,
					end.y - start.y);
			g2.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
			g2.setColor(Color.BLACK);
			g2.drawRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
			for (Rectangle r1 : recs) {
				Rectangle temp = r1.intersect(r);
				g2.setColor(Color.GREEN);
				g2.fillRect(temp.getX(), temp.getY(), temp.getWidth(),
						temp.getHeight());
				g2.setColor(Color.BLACK);
				g2.drawRect(temp.getX(), temp.getY(), temp.getWidth(),
						temp.getHeight());
			}
			if (button == 1) {
				for (Rectangle r1 : recs) {
					Rectangle temp = r1.intersect(r);
					g2.setColor(Color.RED);
					g2.fillRect(temp.getX(), temp.getY(), temp.getWidth(),
							temp.getHeight());
					g2.setColor(Color.BLACK);
					g2.drawRect(temp.getX(), temp.getY(), temp.getWidth(),
							temp.getHeight());
				}
			}
			if (button1 == 1) {
				if (recs.size() + 1 > 1) {
					union = recs.get(0);
					for (Rectangle tempR : recs) {
						for (Rectangle tempR1 : recs) {
							Rectangle temp = tempR.union(tempR1);
							union = union.union(temp);
						}
					}
					union = union.union(r);
					g2.setColor(Color.BLACK);
					g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_BUTT,
							BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f));

					g2.drawRect(union.getX(), union.getY(), union.getWidth(),
							union.getHeight());
				}
			}
			if (button2 == 1) {
				if (recs.isEmpty()) {
					g2.setColor(Color.BLUE);
					g2.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
					g2.setColor(Color.BLUE);
					g2.drawRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
				} else {
					boolean common = true;
					for (int i = 0; i < recs.size() && common == true; i++) {
						common = r.overlaps(recs.get(i));
					}
					if (common) {
						Rectangle intersect = r.intersect(recs.get(0));
						for (Rectangle r3 : recs) {
							intersect = intersect.intersect(r3);
						}
						g2.setColor(Color.BLUE);
						g2.fillRect(intersect.getX(), intersect.getY(),
								intersect.getWidth(), intersect.getHeight());
						g2.drawRect(intersect.getX(), intersect.getY(),
								intersect.getWidth(), intersect.getHeight());
					}
				}
			}
		}
		// Clears the screen by clearing the union rectangle and or the rectangles themselves.
		if (button3 == 1) {
			if (union != null) {
				g.clearRect(union.getX(), union.getY(), union.getWidth(),
						union.getHeight());
				for (Rectangle rect : recs) {
					g.clearRect(rect.getX(), rect.getY(), rect.getWidth(),
							rect.getHeight());
				}
			} else {
				for (Rectangle rect : recs) {
					g.clearRect(rect.getX(), rect.getY(), rect.getWidth(),
							rect.getHeight());
				}
			}
			button3 = 0;
			recs.clear();
		}
	}
	
	// Is used to call paint in the main method.
	public void paint() {
		repaint();
	}

	public class RectangleMouseListener extends MouseInputAdapter {
		// A listener for the mouse.
		@Override
		public void mousePressed(MouseEvent e) {
			start = e.getPoint();
			end = start;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Rectangle r = new Rectangle(start.x, start.y, e.getX() - start.x,
					e.getY() - start.y);
			recs.add(r);
			start = null;
			end = null;
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// Main problem with this working was that I did not add a listener
			// for it.
			end = e.getPoint();
			repaint();
		}

	}

	public static void main(String args[]) {
		// Main method sets up the UI and defines what buttons/checkboxes do.
		final RectangleLabel recLabel = new RectangleLabel();
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("Rectangles");
		mainFrame.setSize(new Dimension(600, 600));
		mainFrame.setLayout(new BorderLayout());
		JPanel buttons = new JPanel();
		JCheckBox intersection = new JCheckBox("Draqw Intersection");
		JCheckBox union = new JCheckBox("Draw Union");
		JCheckBox commonArea = new JCheckBox("Draw Common Area");
		JButton clearScr = new JButton("Clear Screen");
		JButton saveImg = new JButton("Save Image");
		buttons.setLayout(new GridLayout(1, 5));
		// Will add a listener for every button.
		intersection.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					button = 1;
					recLabel.paint();
				} else {
					button = -1;
					recLabel.paint();
				}
			}
		});
		union.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					button1 = 1;
					recLabel.paint();
				} else {
					button1 = -1;
					recLabel.paint();
				}
			}
		});
		commonArea.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					button2 = 1;
					recLabel.paint();
				} else {
					button2 = -1;
					recLabel.paint();
				}
			}
		});
		clearScr.setEnabled(true);
		clearScr.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				button3 = 1;
				recLabel.paint();
			}
		});
		saveImg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(recLabel);
				File file = fc.getSelectedFile();
				BufferedImage bf = new BufferedImage(recLabel.getWidth(),
						recLabel.getHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics g = bf.getGraphics();
				recLabel.paintAll(g);
				try {
					ImageIO.write(bf, "png", file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		buttons.add(intersection);
		buttons.add(union);
		buttons.add(commonArea);
		buttons.add(clearScr);
		buttons.add(saveImg);
		mainFrame.add(buttons, "South");
		mainFrame.add(recLabel);
		mainFrame.setVisible(true);
	}
}

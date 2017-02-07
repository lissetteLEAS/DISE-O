// Antonio García Castillo
// Ingeniero Técnico en Informática de Gestión
// Proyecto : Herramientas web para la enseñanza
//				  de protocolos de comunicación



import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.net.*;
import java.util.*;
import java.util.Random.*;
import javax.swing.text.*;
import java.util.Vector.*;

public class Bellman extends JApplet
{


   
	ClassGrafico grafico = new ClassGrafico(this);
	Controles       menu = new Controles(this);
	Informacion     info = new Informacion();
	
	public void init()
	 
	{
    setBackground(Color.white);
	 getContentPane().add(menu,BorderLayout.SOUTH);
	 getContentPane().add(grafico,BorderLayout.CENTER);
	 getContentPane().add(info,BorderLayout.EAST);
	
	}
	
	
	
}	

//Prueba
//clase encargada de suministrar informacion al usuario
// sobre las diferentes teclas de la aplicacion
// ademas de visualizar los la tabla de ruteo del nodo
// elejido, asi como los paquetes de información que este
// recibe durante la ejecución del algoritmo.

class Informacion extends JComponent{ 
  Border  border = BorderFactory.createCompoundBorder(
                   BorderFactory.createEtchedBorder(
                   Color.white,Color.blue),BorderFactory.createEmptyBorder(4,4,4,4));

  Image barra;
  Image panel_info;
  Font arial12 = new Font("Arial",Font.PLAIN,12);
  Font arial   = new Font("Arial",Font.ITALIC,11);
  Font helvb   = new Font("Helvetica",Font.BOLD,12);
  FontMetrics ar2metrics=getFontMetrics(arial);
  FontMetrics ar1metrics=getFontMetrics(arial12);
  
  
  
  int posicion = 0;
  
  Graphics graphics;
  Image image;
  MediaTracker tracker;



  //constructor
  Informacion()
  {
  setDoubleBuffered(false);
  setBorder(border);
  URL ur=Informacion.class.getResource("barra.gif");
  barra=Toolkit.getDefaultToolkit().getImage(ur);
  ur=Informacion.class.getResource("informacion.gif");
  panel_info=Toolkit.getDefaultToolkit().getImage(ur);
  tracker=new MediaTracker(this);
  tracker.addImage(panel_info,0);
  tracker.addImage(barra,1);
  repaint();

  }




  //metodo para que el componente utilice un tamaño predeterminado
  
  public Dimension getPreferredSize()
  { return new Dimension(330,Short.MAX_VALUE);
  }





  public void paintComponent(Graphics g)
  {
  if (image==null) 
   {image=createImage(getSize().width,getSize().height);
    graphics=(Graphics)image.getGraphics();
    clear();
    inicio();
   }
   g.drawImage(image,0,0,null);
  }  


// metodo que pinta la pantalla con la informacion
  public void inicio()
  {
   try{
   tracker.waitForID(0);}
   catch(InterruptedException e){}
   graphics.drawImage(panel_info,2,2,this);
   repaint();
  }










// limpia el area con un color determinado
  public void clear()
  {
    graphics.setColor(new Color(220,250,255));
    graphics.fillRect(0,0,getSize().width,getSize().height);
    repaint();

  }


// dibuja la cabecera donde se colocará la 
// tabla de encaminamiento del nodo elegido, 
// el parametro "elementos" indica el tamaño de la 
// tabla de encaminamiento, (tantos como nodos haya en la red)
  
  public void cabecera(int elementos,int nodo)
  { 
   graphics.setColor(new Color(220,250,255));
   graphics.fillRect(0,0,getSize().width,200);
    
   graphics.setColor(Color.black);
   try{
   tracker.waitForID(1);}
   catch(InterruptedException e){}
   graphics.drawImage(barra,20,40,this);
   graphics.setFont(helvb);
   char nd=(char)((int)'A'+nodo);
  
   graphics.drawString(""+nd,185,53);
   graphics.setFont(arial);
   
 
   graphics.drawString("Tabla de encaminamiento",60,80);
   for (int i=0;i<elementos;i++)
   {char c=(char)((int)'a'+i);
    graphics.setColor(Color.red);
    graphics.drawString(""+c,70+(i*21),92);
    graphics.setColor(Color.black);
    graphics.drawRoundRect(60+(i*21),95,20,20,3,3);
    graphics.drawRoundRect(60+(i*21),116,20,20,3,3);}
   repaint(); 
  } 

 






// dibuja un paquete procedente del nodo "p",
// y la informacion que dicho paquete lleva
// el parametro "num_paquete", sirve a la hora
// de posicionar el dibujo en la pantalla, ya que
// se visualizaran, tantos paquetes como le lleguen 
// al nodo

  public void pintar_paquete(String p,int retardo,
                             int num_paquetes,boolean [] destacar,
                             int [] array)
                             
  {String dato=new String();
   if (posicion==0)
   {graphics.setColor(new Color(220,250,255));
    graphics.fillRect(0,205,getSize().width,getSize().height);
   }
   graphics.setColor(Color.blue);
   graphics.setFont(helvb);
   graphics.drawString(""+p,50,225+(posicion*26));
   graphics.setFont(arial);
   graphics.drawString("+"+retardo,25,225+(posicion*26));
   graphics.setColor(Color.black);
 
   for (int i=0;i<12;i++)
   {char c=(char)((int)'a'+i);
    graphics.drawString(""+c,70+(i*21),206);
    graphics.drawRoundRect(60+(i*21),211+(posicion*26),20,20,3,3);
    if (destacar[i]) 
    { graphics.setColor(new Color(253,255,202));
      graphics.fillRoundRect(61+(i*21),212+(posicion*26),19,19,3,3);
    } 
    
    if (array[i]==-1)
     {dato="0";}
    if (array[i]==0)
     {dato="-";}
    if (array[i]>0)
     {dato=""+array[i];}
    graphics.setColor(Color.black);
    graphics.drawString(dato,70+(i*21)-(int)ar2metrics.stringWidth(dato)/2-1,225+(posicion*26));
   }
   posicion++;
   if (posicion>num_paquetes-1) {posicion=0;}
   repaint(); 
  }

  
  
  
  
  
// muestra la informacion de la tabla de encaminamiento
// que se le pasa como parametro
  
  public void mostrar_tabla(int [][] array)
  {int margen;
   String dato=new String();
   graphics.setFont(arial12);
   for (int i=0;i<12;i++)
   { graphics.setColor(Color.white);
     graphics.fillRoundRect(61+(i*21),96,18,19,3,3);
     graphics.fillRoundRect(61+(i*21),117,18,19,3,3);
       graphics.setColor(Color.black);
    
    if (array[0][i]==-1)
     {dato="0";}
    if (array[0][i]==0)
     {dato="-";}
    if (array[0][i]>0)
     {dato=""+array[0][i];}
    margen=(int)ar1metrics.stringWidth(dato)/2-1;
    graphics.drawString(dato,68-margen+(i*21),109);
    if (array[1][i]!=-1)
     dato=""+(char)((int)'a'+array[1][i]);
    else
     dato="-";
    graphics.setColor(Color.blue); 
    graphics.drawString(dato,67+(i*21),131); 
   } 
  repaint();
  
  
  
  }
  
  
  
 


}






//clase controles , contenedora de los botones necesarios
// para la aplicacion

class Controles extends JPanel
{ 

  JToolBar barra = new JToolBar();
  JPanel panel2 = new JPanel();
  JLabel lbltext= new JLabel("Velocidad");
  

  JSlider vel;
  
  
  
  
  
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  //iconos de los botones
  Icon icnuevo = new ImageIcon(
         Bellman.class.getResource("bntnuevo.gif"));
  Icon icnpeso = new ImageIcon(
         Bellman.class.getResource("bntpesos.gif"));
  Icon icnodo  = new ImageIcon(
  			Bellman.class.getResource("bntnodo.gif"));
  Icon icnok   = new ImageIcon(
  			Bellman.class.getResource("bntok.gif"));
  Icon icnrun  = new ImageIcon(
  			Bellman.class.getResource("bntrun.gif"));
 
  Icon iccancel= new ImageIcon(
  			Bellman.class.getResource("bntcancel.gif"));

  int ejemplo =0;
  
  
  
   //botones de la barra
   
  JButton Bntnuevo    = new JButton(icnuevo);
  JButton Bntpesos    = new JButton(icnpeso);
  JButton Bntvalidar  = new JButton(icnok);
  JButton Bntnodo     = new JButton(icnodo);
  JButton Bntrun      = new JButton(icnrun);
  JButton Bntcancel   = new JButton(iccancel);
     
  
  // definicion de bordes
  
  Border border2;
  Border border3;
  Border border4;
  
  
  
  //Objeto padre
  Bellman parent;


  // constructores del objeto
  
   Controles(Bellman myparent)
  { parent=myparent;//los bordes para la GUI
    border2 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),BorderFactory.createEmptyBorder(4,4,4,4));
    border3 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(Color.white,new Color(151, 145, 140)),BorderFactory.createEmptyBorder(3,10,3,10));
    border4 = BorderFactory.createLineBorder(Color.white,1);

    panel2.setBackground(new Color(255,203,220));
    panel2.setBorder(border4);
    lbltext.setFont(new Font("Arial",Font.ITALIC,10));

    panel2.add(lbltext);
    vel=new JSlider(JSlider.HORIZONTAL,0,100,100);
    vel.setBackground(panel2.getBackground());
    Hashtable h= new Hashtable();
    
    
    JLabel cero=new JLabel("0");
    cero.setFont(new Font("Arial",Font.ITALIC,10));
    JLabel cien=new JLabel("100");
    cien.setFont(new Font("Arial",Font.ITALIC,10));
   
    h.put(new Integer (0), cero);
 
    h.put(new Integer (100), cien);
    vel.setLabelTable(h);
    
    
    vel.setPaintLabels(true);
    panel2.add(vel);
    
    
    
    
    
    setBackground(new Color(216, 203, 210));
    setBorder(BorderFactory.createEtchedBorder());
    setLayout(gridBagLayout1);
    barra.setBackground(new Color(216,203,210));
    barra.setBorder(border2);
    //Botones
    //aplicacion de bordes
    Bntnuevo.setBorder(border3);
    Bntpesos.setBorder(border3);
    Bntnodo.setBorder(border3);
    Bntvalidar.setBorder(border3);
    Bntrun.setBorder(border3);
    Bntcancel.setBorder(border3);
 
    //aplicacion de opacidad

    Bntnuevo.setOpaque(false);
    Bntpesos.setOpaque(false);
    Bntnodo.setOpaque(false);
    Bntvalidar.setOpaque(false);

    Bntrun.setOpaque(false);
   
    Bntcancel.setOpaque(false);
    Bntpesos.setEnabled(false);
    Bntnodo.setEnabled(false);
    Bntvalidar.setEnabled(false);
    Bntrun.setEnabled(false);
    // ayuda de los botones
    Bntnuevo.setToolTipText("Crear nueva red");
    Bntpesos.setToolTipText("Crear nuevos pesos");
    Bntvalidar.setToolTipText("Chequear la red");
    Bntnodo.setToolTipText("Selección del nodo");
    Bntrun.setToolTipText("Ejecutar el algoritmo");
    Bntcancel.setToolTipText("Cancelar pasos");

   // metodo de escucha de la barra de desplazamiento
   vel.addChangeListener(new ChangeListener(){
    public void stateChanged(ChangeEvent e){
     parent.grafico.setVelocidad(vel.getValue());
     }}); 
    
    
    
   
   // metodos de escucha de los botones de la barra
   // ------------------------------------------------------------------------ 
    
     Bntnuevo.addActionListener(new ActionListener( ){
      public void actionPerformed(ActionEvent ae) {
        
         parent.grafico.mired.crear();
         parent.grafico.mired.generar_tabla(ejemplo);
         parent.grafico.mired.cargar_tabla();
         ejemplo++;
         if (ejemplo>2) {ejemplo=0;}
         parent.grafico.repaint();
         Bntpesos.setEnabled(true);
         Bntnodo.setEnabled(true);
         Bntvalidar.setEnabled(true); 
       
       
      }
      });
// metodos de escucha de los diferentes botones
    Bntpesos.addActionListener(new ActionListener( ){
      public void actionPerformed(ActionEvent ae) {
          
       parent.grafico.mired.generar_pesos();
       parent.grafico.mired.cargar_tabla();
       if (parent.grafico.mired.getDestino()!=-1)
         parent.info.mostrar_tabla(
            parent.grafico.mired.red[parent.grafico.mired.getDestino()].tabla_ruteo);

       parent.grafico.repaint();
      }
      });

    Bntnodo.addActionListener(new ActionListener( ){
      public void actionPerformed(ActionEvent ae) {
   
       parent.grafico.unlock();
   
    
      }
      });
    
    
     Bntvalidar.addActionListener(new ActionListener( ){
      public void actionPerformed(ActionEvent ae) {
          
       if (parent.grafico.mired.getDestino()!=-1)
        {parent.grafico.lock();
         Bntnuevo.setEnabled(false);
         Bntpesos.setEnabled(false);
         Bntnodo.setEnabled(false);
         Bntrun.setEnabled(true);
         Bntvalidar.setEnabled(false);
        } 
        
      }
      });

     Bntrun.addActionListener(new ActionListener( ){
      public void actionPerformed(ActionEvent ae) {
          
       parent.grafico.simulation=true;
       parent.grafico.inundacion();
       Bntrun.setToolTipText("Siguiente paso..");
      }
      });
    
     Bntcancel.addActionListener(new ActionListener( ){
      public void actionPerformed(ActionEvent ae) {
          
       parent.grafico.simulation=false;
       parent.grafico.lock();
       parent.grafico.mired.setDestino(-1);
       Bntrun.setEnabled(false);
       Bntnuevo.setEnabled(true);
       Bntrun.setToolTipText("Ejecutar el algoritmo");
       parent.info.clear();
       parent.info.inicio();
       
      }
      });
    
    
    
    
    
    
    
   
   // añadir al panel los botones
    barra.add(Bntnuevo, null);
    barra.add(Bntpesos,null);
   
    for (int i=1;i<4;i++)
    {barra.addSeparator();}
   
    barra.add(Bntnodo,null);
    barra.add(Bntvalidar,null);
    barra.addSeparator();
    barra.add(Bntrun,null);
    barra.add(Bntcancel, null);
    
    add(barra, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
           ,GridBagConstraints.WEST,
            GridBagConstraints.CENTER, new Insets(6, 8, 4, 6), 0, 0));
  
    
    
    add(panel2, new GridBagConstraints(1, 0, 1, 3, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.CENTER, new Insets(5, 5, 5, 8), 160, 10));


    
    }


  



}












//clase grafico


//----------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------
//aqui implemento la clase Grafico, que contiene
//Esta clase se encarga de todo lo referente a la manipulacion
//de graficos, tanto cuando el usuario esta dibujando
//como cuando ejecuta la simulacion
//----------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------


class ClassGrafico extends Canvas implements Runnable{
  Bellman parent; //referencia al contenedor padre;
  Redes mired = new Redes(); 
  Vector v; //vector, que contiene los paquetes.
  Thread t; //hilo de ejecucion para la simulacion.
  public boolean simulation=false;
  private boolean graficolock=true;
  
  private int velocidad=100;
   
  private int opcion=0;
  private int hitnode;
 
  // para el doble buffering
  private Image ImagenVirtual;
  private Graphics GraficoVirtual;
  private Dimension TamVirtual;

  public Image PCselec;

  public Image PC;
  //para las fuentes

  Font helvetica = new Font("Helvetica",Font.BOLD,15);

  


 public void lock()
 { 
  graficolock=true;
 }
 
 public void unlock()
 {
  graficolock=false;
 }


 public void setVelocidad(int vlc)
 {
  velocidad=vlc;
 
 }




 
 
// metodo necesario para el doble buffering 
  public void update(Graphics g)
   {Dimension d=new Dimension();
    d.width=getBounds().width;
    d.height=getBounds().height;
   
   
    if ((ImagenVirtual==null)||(d.width!=TamVirtual.width)||(d.height!=TamVirtual.height))
    {
     ImagenVirtual=createImage(d.width,d.height);
     TamVirtual=d;
    
     GraficoVirtual=ImagenVirtual.getGraphics();
    }
   GraficoVirtual.setColor(Color.white);
   GraficoVirtual.fillRect(0,0,d.width,d.height);
   paint(GraficoVirtual);
   g.drawImage(ImagenVirtual,0,0,null);
  } 
  

  

 // constructor
   ClassGrafico(Bellman myparent)
   {
    parent=myparent;
    setBackground(Color.white);
    URL ur=Bellman.class.getResource("pcselected.gif");
    PCselec=Toolkit.getDefaultToolkit().getImage(ur);
    ur=Bellman.class.getResource("pcazul.gif");
    PC=Toolkit.getDefaultToolkit().getImage(ur);
 
    mired.crear();
    repaint();
   }





// metodo que indica sobre que nodo se ha hecho click
  public boolean nodehit(int x,int y,int dist)
  {
    int i;
    for (i=0;i<mired.TAM;i++)
     {if ((Math.pow(x-mired.red[i].posicion.width,2)+
         (Math.pow(y-mired.red[i].posicion.height,2))<dist*dist))
       {hitnode=i;
        return true;
       } 
      } 
   return false;
   }  









// metodo de escucha del raton
 public boolean mouseDown(Event evt,int x,int y)
 {
 
  if (!graficolock)
   {if (nodehit(x,y,15))
     {parent.info.clear();
      mired.setDestino(hitnode);
      parent.info.cabecera(mired.TAM,hitnode);
      parent.info.mostrar_tabla(mired.red[hitnode].tabla_ruteo);
      repaint();}
  }
  return true;  
 }  


 // muestra los diferentes pesos de los enlaces
 // de la red
 public void mostrar_peso(int x,int y,Graphics g)
 {int x1,y1,x2,y2,px,py,incx,incy,margen;
  String peso=new String();
  Font arial = new Font("Arial",Font.ITALIC,9);
  FontMetrics armetrics=getFontMetrics(arial);

  g.setFont(arial);
  px=1;py=1; 
  x1=mired.red[x].posicion.width;
  y1=mired.red[x].posicion.height;
  x2=mired.red[y].posicion.width;
  y2=mired.red[y].posicion.height;
  incx=Math.abs((x1-x2)/2);
  incy=Math.abs((y1-y2)/2);
  peso=""+mired.tabla[x][y];
  margen=(int)armetrics.stringWidth(peso)+2;

  if (x1>=x2)
    px=x2+incx-margen;
  if (x1<x2)
    px=x1+incx;
  if (y1>=y2)
    py=y2+incy-2;
  if (y1<y2)
    py=y1+incy-2;       
  
  
  g.drawString(peso,px,py);
        
   
 
 
 }
 
 
 
 

// metodo encargado de dibujar todos las estructuras
// en pantalla
 
 public void paint(Graphics g){
   
     for (int x=0;x<mired.TAM;x++){
      for (int y=x;y<mired.TAM;y++){
       if (mired.linkgrafo(x,y)==1)
       {g.setColor(Color.black);
        g.drawLine(mired.red[x].posicion.width,mired.red[x].posicion.height,mired.red[y].posicion.width,mired.red[y].posicion.height);
        mostrar_peso(x,y,g);
        }
      }
     }

    if (simulation)
    {try{
     for (int i=0;i<v.size();i++)
     {
      Paquete p1=new Paquete(mired.TAM);
      p1=((Paquete)v.elementAt(i));
      g.setColor(Color.red);
      g.fillOval(p1.origenx-4,p1.origeny-4,8,8);
     } 
     }catch (RuntimeException e){}
    }

   
   
   for(int x=0;x<mired.TAM;x++)
       {if (x!=mired.getDestino()) 
        {g.drawImage(PC,mired.red[x].posicion.width-16,mired.red[x].posicion.height-16,this);}
       else
        {g.drawImage(PCselec,mired.red[x].posicion.width-16,mired.red[x].posicion.height-16,this);}
         
       };
    
   g.setFont(helvetica);
   for(int i=0;i<mired.TAM;i++)
     {
     g.setColor(Color.blue);
     g.drawString(intToString(i),mired.red[i].posicion.width-14,mired.red[i].posicion.height-10);
     }


   




    
 }


 public String intToString(int i) 
    {
        char c=(char)((int)'a'+i);
        return ""+c;
    }

 
 
 
 // metodo necesario para el algoritmo Bellman Ford
 // cada nodo utiliza la inundacion para la transmi
 //sion de su tabla de ruteo
 public void inundacion()
 {
  v=new Vector();
  parent.info.cabecera(mired.TAM,mired.getDestino());
  parent.info.mostrar_tabla(mired.red[mired.getDestino()].tabla_ruteo);
  parent.info.repaint();
  for (int i=0;i<mired.TAM;i++)
  {for (int j=0;j<mired.TAM;j++)
    {
    if (mired.linkgrafo(i,j)==1)
     {enviar_paquete(i,j);}
    }
  }
  t=new Thread(this);
  t.setPriority(9);
  t.start();
 }    
 
 
// este metodo asigna a las variables del paquete
// las coordenadas necesarias para llegar desde un
// origen a un destino, asi como la informacion a 
//  transportar
public void enviar_paquete(int origen,int destino)
{float l;
 int dx,dy;

 Paquete p_aux=new Paquete(mired.TAM);
 p_aux.origen=origen;
 p_aux.destino=destino;
 p_aux.origenx=mired.red[origen].posicion.width;
 p_aux.origeny=mired.red[origen].posicion.height;
 p_aux.destinox=mired.red[destino].posicion.width;
 p_aux.destinoy=mired.red[destino].posicion.height;
 dx=Math.abs(p_aux.destinox-p_aux.origenx);
 dy=Math.abs(p_aux.destinoy-p_aux.origeny);
 
 
 p_aux.incx=dx/20;
 p_aux.incy=dy/20;
 
 p_aux.setPaquete(mired.red[origen].tabla_ruteo[0]);  
 v.add(p_aux);
}

 
 
 //Algoritmo de Bellman Ford
 
 public void run()
 {
  int ok;
  int num_paquetes=mired.enlaces();
  boolean [] destacar = new boolean [mired.TAM];
  while (!v.isEmpty())
  {if (velocidad<100)
   {
   try  {
    Thread.sleep(100-velocidad);
    }catch (InterruptedException e){}
   }
  
  
   Paquete p1=new Paquete(mired.TAM);
   p1=((Paquete)v.elementAt(0));
   v.removeElementAt(0);
   ok=movimiento(p1);

   repaint(p1.origenx-15,p1.origeny-15,30,30);
   if (ok==0)
    {v.add(p1);}
   else
    {int retardo;// si llega el paquete a su destino
    //habra que actualizar las tablas de ruteo
    //dependiendo de la informacion de este
     for (int i=0;i<mired.TAM;i++)
     {destacar[i]=false;
      if (p1.informacion[i]>0)  
       {  
         retardo=p1.informacion[i]+mired.red[p1.destino].tabla_ruteo[0][p1.origen];
          
         if (retardo<mired.red[p1.destino].tabla_ruteo[0][i])
           {    mired.red[p1.destino].tabla_ruteo[0][i]=retardo;
                mired.red[p1.destino].tabla_ruteo[1][i]=mired.red[p1.destino].tabla_ruteo[1][p1.origen]; 
                if (p1.destino==mired.getDestino())
                 destacar[i]=true;
            } 
         if (mired.red[p1.destino].tabla_ruteo[0][i]==0)
            {mired.red[p1.destino].tabla_ruteo[0][i]=retardo;
             mired.red[p1.destino].tabla_ruteo[1][i]=p1.origen;
             if (p1.destino==mired.getDestino())
                 destacar[i]=true;
               
            }
       
         } 
      }
     
     if (p1.destino==mired.getDestino()) 
      { parent.info.mostrar_tabla(mired.red[mired.getDestino()].tabla_ruteo);
        parent.info.pintar_paquete(intToString(p1.origen),
                                  mired.red[p1.destino].tabla_ruteo[0][p1.origen],
                                  num_paquetes,
                                  destacar,p1.informacion);}
        parent.info.repaint();
      }
 

      
      
      } 
     
 

 
   
 }
 
 //movimiento del paquete, para posicionarlo
 // en las coordenadas (x,y) de la pantalla
public int movimiento(Paquete p)
{int x1,y1,x2,y2;
 x1=p.origenx;
 y1=p.origeny;
 x2=p.destinox;
 y2=p.destinoy;
 if ((x1!=x2)|(y1!=y2))
   {
    if (x1<x2)
     {p.origenx+=p.incx;}
    if (x1>x2)
     {p.origenx-=p.incx;}
    if (y1<y2)
     {p.origeny+=p.incy;}
    if (y1>y2)
     {p.origeny-=p.incy;}
    return 0;
   }
  else
   {return 1;}      
     
 
 }
 
 
 
 
 
 
 
 
 
}


// clase Paquete, estructura necearia para representar
// un objeto del tipo Paquete



class Paquete
{ int origen;
  int destino;
  int origenx;
  int origeny;
  int destinox;
  int destinoy;
  float incx;
  float incy;
  int [] informacion;
  
  Paquete(int tam)
  {informacion=new int[tam];
  } 
  
  public void setPaquete(int [] arraysource)
  {
   System.arraycopy(arraysource,0,informacion,0,arraysource.length);
  }   
  
  
}




// clase representativa de cada nodo
class Nodo
{
 
 Dimension posicion;
 int [][] tabla_ruteo;

 Nodo(int tam)
 {
  this.tabla_ruteo=new int [2][tam];
 } 


}


// clase Redes---------------------------------------------------





class Redes
{
                  
  int tabla[][]=new int[TAM][TAM];



  static int TAM=12; //numero de nodos disponibles
  private int[][] grafo; //estructura grafo 
 
  private int DESTINO=-1; //nodo a analizar;
  public Nodo[] red;
  long seed=1;
  Random randnums=new Random(seed);
   

 
  // Metodos para manejar el destino 
  // (nodo) a examinar durante la
  // ejecucion del algoritmo
  public void setDestino(int destino)
  {
   this.DESTINO=destino;
  }
  
  public int getDestino()
  {
   return DESTINO;
  } 



 


//Constructor, encargado de inicializar las estructuras de datos
//que maneja la clase.
  
  public void crear()
  {
  
 

   grafo=new int[TAM][TAM];
   red=new Nodo[TAM];
   
   for (int  i=0;i<TAM;i++)
    {
     red[i]=new Nodo(TAM);
     }
   int x=0,y=0,i,j;
   
   DESTINO=-1;
   
   for(i=0;i<TAM;i++)
    {for (j=0;j<TAM;j++)
     {grafo[i][j]=0;}
   }  
  
   for(i=0;i<TAM;i++){
    red[i].posicion=new Dimension((100*x)+50,(120*y)+60);
     
       if ((i==3)||(i==7)||(i==11)||(i==15))
       { y++;
         x=0;}
        else { x++;} 
       }
 
}    
  
  
  
  
  // genera pesos aleatoriamente
     
 public void generar_pesos()
 {

  for (int i=0;i<TAM;i++)
   for (int j=i;j<TAM;j++)
     if (tabla[i][j]>0)
      {
       int peso=randnums.nextInt(14)+1;
       tabla[i][j]=peso;
       tabla[j][i]=peso;
      }  
 }
 
 
 
// metodo que carga la matriz de datos
// cada nodo, se inicializara, con su 
// correspondiente informacion 
 public void cargar_tabla()
 {   
  for (int i=0;i<TAM;i++)
      {System.arraycopy(tabla[i],0,red[i].tabla_ruteo[0],0,12); 
       for (int j=0;j<TAM;j++)
       {if (tabla[i][j]>0)
         red[i].tabla_ruteo[1][j]=j;
        else
         red[i].tabla_ruteo[1][j]=-1; 
        } 
      } 
   
   for(int i=0;i<TAM;i++)
    for (int j=0;j<TAM;j++)
      if (red[i].tabla_ruteo[0][j]>0) 
       {grafo[i][j]=1;
        grafo[j][i]=1;}
     
  
 }  
//metodo que contiene los 3 ejemplos disponibles

 public void generar_tabla(int ejemplo)
 {
   int ejemplos[][][]={ { {-1,12,0,0,2,0,0,0,0,9,0,0},//a
                          {12,-1,14,0,0,0,6,0,0,0,0,0},//b
                          {0,14,-1,16,4,0,0,0,0,0,0,0},//c
                          {0,0,16,-1,0,0,0,5,0,0,0,0},//d
                          {2,0,4,0,-1,0,0,0,4,0,0,0},//e
                          {0,0,0,0,0,-1,5,0,0,0,0,0},//f
                          {0,6,0,0,0,5,-1,3,0,0,0,0},//g
                          {0,0,0,5,0,0,3,-1,0,2,0,3},//h
                          {0,0,0,0,4,0,0,0,-1,5,0,0},//i
                          {9,0,0,0,0,0,0,2,5,-1,2,0},//j
                          {0,0,0,0,0,0,0,0,0,2,-1,2},//k
                          {0,0,0,0,0,0,0,3,0,0,2,-1}},//l
                         
                         {{-1,4,0,0,5,0,13,0,0,9,0,0},
                          {4,-1,2,0,6,0,0,0,0,0,0,0},
                          {0,2,-1,5,0,8,3,1,0,0,0,0},
                          {0,0,5,-1,0,0,0,7,0,0,0,0}, 
                          {5,6,0,0,-1,0,0,0,9,3,0,0},
                          {0,0,8,0,0,-1,2,0,0,0,11,0},
                          {13,0,3,0,0,2,-1,7,8,0,1,0},
                          {0,0,1,7,0,0,7,-1,0,0,3,5},
                          {0,0,0,0,9,0,8,0,-1,1,0,0},
                          {9,0,0,0,3,0,0,0,1,-1,6,0},
                          {0,0,0,0,0,11,1,3,0,6,-1,4},
                          {0,0,0,0,0,0,0,5,0,0,4,-1}},
                         
                         {{-1,2,0,0,0,6,0,0,0,0,0,0},
                          {2,-1,4,0,0,12,1,0,0,0,0,0},
                          {0,4,-1,1,0,0,6,12,0,0,0,0},
                          {0,0,1,-1,0,0,0,3,0,0,0,0},
                          {0,0,0,0,-1,0,0,0,2,3,0,0},
                          {6,12,0,0,0,-1,3,0,0,4,1,0},
                          {0,1,6,0,0,3,-1,0,0,0,6,9},
                          {0,0,12,3,0,0,0,-1,0,0,0,2},
                          {0,0,0,0,2,0,0,0,-1,6,0,0},
                          {0,0,0,0,3,4,0,0,6,-1,2,0},
                          {0,0,0,0,0,1,6,0,0,2,-1,5},
                          {0,0,0,0,0,0,9,2,0,0,5,-1}}}; 


  for (int i=0;i<TAM;i++)
   System.arraycopy(ejemplos[ejemplo][i],0,tabla[i],0,12);
  
  
  
    
 
 
 }







//indica si 2 nodos estan conectados mediante un enlace


 public int linkgrafo(int nodo,int adyacente)
     {
     return grafo[nodo][adyacente];
     }


// indica el numero de enlaces que tiene un nodo
public int enlaces()
 {int enlaces=0;
  for(int i=0;i<TAM;i++)
   {if (linkgrafo(DESTINO,i)==1)
      enlaces++;
   }
   return enlaces;    
  }




}

 



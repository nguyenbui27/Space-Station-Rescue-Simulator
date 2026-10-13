import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/** Small bitmap renderer. Game objects still use their original coordinates. */
public final class PixelTheme {
    private static final Map<Character,String> FONT = new HashMap<Character,String>();
    static {
        String letters="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String[] rows={
            "01110/10001/10001/11111/10001/10001/10001", "11110/10001/10001/11110/10001/10001/11110",
            "01111/10000/10000/10000/10000/10000/01111", "11110/10001/10001/10001/10001/10001/11110",
            "11111/10000/10000/11110/10000/10000/11111", "11111/10000/10000/11110/10000/10000/10000",
            "01111/10000/10000/10111/10001/10001/01111", "10001/10001/10001/11111/10001/10001/10001",
            "11111/00100/00100/00100/00100/00100/11111", "00111/00010/00010/00010/10010/10010/01100",
            "10001/10010/10100/11000/10100/10010/10001", "10000/10000/10000/10000/10000/10000/11111",
            "10001/11011/10101/10101/10001/10001/10001", "10001/11001/10101/10011/10001/10001/10001",
            "01110/10001/10001/10001/10001/10001/01110", "11110/10001/10001/11110/10000/10000/10000",
            "01110/10001/10001/10001/10101/10010/01101", "11110/10001/10001/11110/10100/10010/10001",
            "01111/10000/10000/01110/00001/00001/11110", "11111/00100/00100/00100/00100/00100/00100",
            "10001/10001/10001/10001/10001/10001/01110", "10001/10001/10001/10001/10001/01010/00100",
            "10001/10001/10001/10101/10101/10101/01010", "10001/10001/01010/00100/01010/10001/10001",
            "10001/10001/01010/00100/00100/00100/00100", "11111/00001/00010/00100/01000/10000/11111",
            "01110/10001/10011/10101/11001/10001/01110", "00100/01100/00100/00100/00100/00100/01110",
            "01110/10001/00001/00010/00100/01000/11111", "11110/00001/00001/01110/00001/00001/11110",
            "00010/00110/01010/10010/11111/00010/00010", "11111/10000/10000/11110/00001/00001/11110",
            "01110/10000/10000/11110/10001/10001/01110", "11111/00001/00010/00100/01000/01000/01000",
            "01110/10001/10001/01110/10001/10001/01110", "01110/10001/10001/01111/00001/00001/01110"};
        for(int i=0;i<letters.length();i++) FONT.put(letters.charAt(i),rows[i]);
        FONT.put(':',"00000/00100/00100/00000/00100/00100/00000");
        FONT.put('/',"00001/00001/00010/00100/01000/10000/10000");
        FONT.put('.',"00000/00000/00000/00000/00000/00100/00100");
        FONT.put('!',"00100/00100/00100/00100/00100/00000/00100");
        FONT.put('-',"00000/00000/00000/11111/00000/00000/00000");
    }
    public static void text(Graphics2D g,String text,int x,int baseline,int rgb) {
        g.setColor(new Color(rgb));
        for(char ch:text.toUpperCase(Locale.ROOT).toCharArray()) {
            String glyph=FONT.get(ch);
            if(glyph!=null){String[] rows=glyph.split("/");for(int y=0;y<7;y++)for(int col=0;col<5;col++)
                if(rows[y].charAt(col)=='1')g.fillRect(x+col*2,baseline-14+y*2,2,2);}
            x+=12;
        }
    }
    public static void background(Graphics2D g,int w,int h) {
        g.setColor(new Color(0x101522));g.fillRect(0,0,w,h);
        Random r=new Random(12);for(int i=0;i<65;i++){
            int x=r.nextInt(w/4)*4,y=80+r.nextInt(Math.max(1,(h-140)/4))*4;
            g.setColor(new Color(i%6==0?0x8293a8:0x344358));g.fillRect(x,y,2,2);
        }
        g.setColor(new Color(0x1c2738));g.fillRect(0,0,w,66);g.fillRect(0,h-36,w,36);
    }
    public static void sprite(Graphics2D g,String kind,Rectangle b,String state) {
        String[] pixels;
        if(kind.equals("Triangle"))pixels=new String[]{
            "  rr      "," rrrr     "," rwwwr    ","wwwwwccw  ","wwwwwwwwww","wwwwwccw  "," rwwwr    "," rrrr     ","  rr      "};
        else if(kind.equals("Diamond"))pixels=new String[]{
            "       ww       ","       ww       ","bbb  wwwwww  bbb","bbbwwwwggwwwwbbb","bbb  wwggww  bbb","bbb  wwwwww  bbb","      wwww      ","       gg       ","       gg       "};
        else pixels=new String[]{"  www  "," wgggw ","wgggggw","wgggggw","wgggggw"," wgggw ","  www  "};
        int rows=pixels.length,cols=pixels[0].length();
        for(int y=0;y<rows;y++)for(int x=0;x<cols;x++){
            char c=pixels[y].charAt(x);if(c==' ')continue;
            int rgb=c=='w'?0xd8e1e8:c=='r'?0xc86b75:c=='b'?0x446584:c=='c'?0x78c9e1:
                state.equals("yellow")?0xe5c56c:state.equals("red")?0xd47777:0x8cc9a7;
            g.setColor(new Color(rgb));
            int left=b.x+x*b.width/cols,top=b.y+y*b.height/rows;
            g.fillRect(left,top,b.x+(x+1)*b.width/cols-left,b.y+(y+1)*b.height/rows-top);
        }
    }
    public static void main(String[] args)throws Exception {
        BufferedImage out=new BufferedImage(800,600,BufferedImage.TYPE_INT_RGB);
        Graphics2D g=out.createGraphics();background(g,800,600);
        text(g,"REFUEL THE STATION THREE TIMES.",20,28,0xe2e6ed);
        text(g,"ROUND 1 / 3     FUEL 5     SCORE 0",20,54,0x8cc9a7);
        text(g,"K/J MOVE   SPACE FIRE   P PAUSE   R RESTART   ESC EXIT",20,584,0x99a9bd);
        sprite(g,"Triangle",new Rectangle(25,280,30,40),"red");
        sprite(g,"Diamond",new Rectangle(470,150,80,50),"green");
        sprite(g,"Circle",new Rectangle(230,285,30,30),"blue");
        g.dispose();javax.imageio.ImageIO.write(out,"png",new java.io.File("pixel-preview.png"));
    }
}

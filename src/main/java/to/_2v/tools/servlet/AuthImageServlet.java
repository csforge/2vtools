package to._2v.tools.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import to._2v.tools.Constant;
import to._2v.tools.util.StringUtil;

/**
 * 
 * @author prx
 *
 */
public class AuthImageServlet extends HttpServlet{
	
	private static final long serialVersionUID = -6654230287329762287L;
//	private Font mFont = new Font("Arial Black", Font.PLAIN, 16);
	private Font mFont;
	
	public void init(){
		String fontname = this.getInitParameter("font-name");
		String fontstyle = this.getInitParameter("font-style");
		int fontsize = getIntProperty(getInitParameter("font-size"));
		int font = Font.PLAIN;
		if (StringUtil.isBlank(fontname))
			fontname = "Arial Black";
		if (fontsize < 1)
			fontsize = 16;
		if (StringUtil.isNotBlank(fontstyle)) {
			if (fontstyle.equalsIgnoreCase("bold"))
				font = Font.BOLD;
			else if (fontstyle.equalsIgnoreCase("italic"))
				font = Font.ITALIC;
		}
			
		 mFont = new Font(fontname, font, fontsize);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws java.io.IOException, ServletException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws java.io.IOException, ServletException {
		execute(request, response);
	}
	
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	 

	private String getRandomChar() {
		int rand = (int) Math.round(Math.random() * 1);
		long itmp = 0;
		char ctmp = '\u0000';
		switch (rand) {
		case 1: // 生成小寫的情況
			itmp = Math.round(Math.random() * 25 + 97);
			ctmp = (char) itmp;
			return String.valueOf(ctmp);
		default: // 生成數字的情況
			itmp = Math.round(Math.random() * 9);
			return String.valueOf(itmp);
		}
	}
	
	private void execute(HttpServletRequest request, HttpServletResponse response) {
		String crumb = request.getParameter("crumb");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		int width = 75, height = 18;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200, 250));
		g.fillRect(1, 1, width - 1, height - 1);
		g.setColor(new Color(102, 102, 102));
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(mFont);

		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int xl = random.nextInt(6) + 1;
			int yl = random.nextInt(12) + 1;
			g.drawLine(x, y, x + xl, y + yl);
		}
		for (int i = 0; i < 70; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int xl = random.nextInt(12) + 1;
			int yl = random.nextInt(6) + 1;
			g.drawLine(x, y, x - xl, y - yl);
		}

		String sRand = "";
		for (int i = 0; i < 4; i++) {
			String tmp = getRandomChar();
			sRand += tmp;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(tmp, 15 * i + 10, 15);
		}

		HttpSession session = request.getSession(true); // 保存驗證碼的String
		if(StringUtil.isNotBlank(crumb))
			session.setAttribute(Constant.RAND+crumb, sRand);
		else
			session.setAttribute(Constant.RAND, sRand);
		g.dispose();
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int getIntProperty(String str) {
		try {
			if (StringUtil.isNotBlank(str))
				return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return -1;
		}
		return -1;
	}
}

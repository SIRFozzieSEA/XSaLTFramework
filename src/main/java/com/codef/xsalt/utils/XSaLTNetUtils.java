package com.codef.xsalt.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTNetUtils {

	/**
	 * Main Constructor
	 */
	public XSaLTNetUtils() {

		// try
		// {
		// XSaLTNetUtils oNU = new XSaLTNetUtils();
		// oNU.getLinksysLanIP("192.168.1.1", "admin", "admin", "3.0.0.19");
		// }
		// catch (IOException e)
		// {
		//
		// }

	}

	/**
	 * The method that gets the content of a webpage and returns it as a string
	 * 
	 * @param requestURL The webpage URL
	 * @return The HTML content as string
	 * @throws IOException
	 */
	public static String readStringFromURL(String requestURL) throws IOException {
		URL u = new URL(requestURL);
		try (InputStream in = u.openStream()) {
			return new String(in.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

//	/**
//	 * The method that gets the Linksys router IP address
//	 * 
//	 * @param _sRouterHost
//	 *            The router host or IP (null is default IP for router)
//	 * @param _sRouterUserName
//	 *            The router admin username (null is default for router)
//	 * @param _sRouterPassword
//	 *            The router admin password (null is default for router)
//	 * @param _sVersion
//	 *            The router firmware version
//	 * @return The LAN IP address
//	 * @throws IOException
//	 */
//	public String getLinksysLanIP(String _sRouterHost, String _sRouterUserName, String _sRouterPassword, String _sVersion) throws IOException
//	{
//		if (_sRouterHost == null)
//		{
//			_sRouterHost = "192.168.1.1";
//		}
//
//		if (_sRouterUserName == null)
//		{
//			_sRouterUserName = "admin";
//		}
//
//		if (_sRouterPassword == null)
//		{
//			_sRouterPassword = "admin";
//		}
//
//		StringBuffer oRouterStatusPageStringbuffer = new StringBuffer();
//		java.net.Authenticator.setDefault(new XSaLTNetworkAuthenticator(_sRouterUserName, _sRouterPassword));
//
//		URL oUrl = null;
//
//		if (_sVersion.equalsIgnoreCase("3.0.0.19"))
//		{
//			// TMA Corporate Router
//			oUrl = new URL("http://" + _sRouterHost + "/home.htm");
//		}
//		if (_sVersion.equalsIgnoreCase("1.0.04"))
//		{
//			// Steve's Home Router
//			oUrl = new URL("http://" + _sRouterHost + "/index.stm?title=Status-Status");
//		}
//
//		//		if (_sVersion.equalsIgnoreCase("1.52.15"))
//		//		{
//		//			// Steve's Router
//		//			oUrl = new URL("http://" + _sRouterHost + "/RouterStatus.htm");
//		//		}
//		//		else if (_sVersion.equalsIgnoreCase("1.52.2"))
//		//		{
//		//			// Lance's Router
//		//			oUrl = new URL("http://" + _sRouterHost + "/StaRouter.htm");
//		//		}
//		//		else if (_sVersion.equalsIgnoreCase("2.0.10"))
//		//		{
//
//		//		}
//		//		else if (_sVersion.equalsIgnoreCase("2.51.4"))
//		//		{
//		//			// TMA Corporate Router
//		//			oUrl = new URL("http://" + _sRouterHost + "/Status_Router.htm");
//		//		}
//		//		if (_sVersion.equalsIgnoreCase("SPC"))
//		//		{
//		//			oUrl = new URL("http://" + _sRouterHost + "/index.stm?title=Status-Status");
//		//		}
//		//		else
//		//		{
//		//			oUrl = new URL("http://" + _sRouterHost + "/Status.htm");
//		//		}
//
//		BufferedReader oBufferedReader = new BufferedReader(new InputStreamReader(oUrl.openStream()));
//
//		String oReadLine;
//		while ((oReadLine = oBufferedReader.readLine()) != null)
//		{
//			oRouterStatusPageStringbuffer.append(oReadLine);
//		}
//		oBufferedReader.close();
//
//		String sTempRouter = "";
//		String sRouterString = oRouterStatusPageStringbuffer.toString().replaceAll("&nbsp;", " ").replaceAll("\\s+", " ").replaceAll(">\\s+", ">").replaceAll("\\s+<", "<")
//				.replaceAll(">\\s<", "><");
//		String asTableData[] = sRouterString.split(">");
//
//		if (_sVersion.equalsIgnoreCase("3.0.0.19"))
//		{
//			sTempRouter = asTableData[700].substring(0, asTableData[700].indexOf("</td"));
//		}
//
//		if (_sVersion.equalsIgnoreCase("1.0.04"))
//		{
//			sTempRouter = asTableData[412].substring(0, asTableData[412].indexOf("</font"));
//		}
//
//		//				for (int i = 0; i < asTableData.length; i++)
//		//				{
//		//					sysout(i + " = '" + asTableData[i] + "'");
//		//				}
//
//		//		if (_sVersion.equalsIgnoreCase("1.52.15"))
//		//		{
//		//			// Steve's Router
//		//			String asTableData[] = oRouterStatusPageStringbuffer.toString().split("<B>");
//		//			sTempRouter = asTableData[7].substring(0, asTableData[7].indexOf("</B>"));
//		//			sTempRouter = sTempRouter.trim();
//		//		}
//		//		else if (_sVersion.equalsIgnoreCase("1.52.2"))
//		//		{
//		//			// Lance's Router
//		//			String asTableData[] = oRouterStatusPageStringbuffer.toString().split("<B>");
//		//			sTempRouter = asTableData[14].substring(0, asTableData[14].indexOf("</B>"));
//		//			sTempRouter = sTempRouter.trim();
//		//		}
//		//		else if (_sVersion.equalsIgnoreCase("2.0.10"))
//		//		{
//		//			// TMA Corporate Router
//		//			sTempRouter = oRouterStatusPageStringbuffer.substring(oRouterStatusPageStringbuffer.indexOf("WAN1 IP"),
//		//					oRouterStatusPageStringbuffer.indexOf("WAN2 IP"));
//		//			String sIpQue = "<td width=\"32%\">";
//		//			sTempRouter = sTempRouter.substring(sTempRouter.indexOf(sIpQue) + sIpQue.length(), sTempRouter.length());
//		//			sTempRouter = sTempRouter.substring(0, sTempRouter.indexOf(" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
//		//			sTempRouter = sTempRouter.trim();
//		//		}
//		//		else if (_sVersion.equalsIgnoreCase("2.51.4"))
//		//		{
//		//			// TMA Corporate Router
//		//			String asTableData[] = oRouterStatusPageStringbuffer.toString().split("<B>");
//		//			sTempRouter = asTableData[7].substring(0, asTableData[7].indexOf("</B>"));
//		//			sTempRouter = sTempRouter.trim();
//		//
//		//		}
//		//		else if (_sVersion.equalsIgnoreCase("SPC"))
//		//		{
//		//			// TMA Corporate Router
//		//			String asTableData[] = oRouterStatusPageStringbuffer.toString().split("<font style=\"font-size: 8pt\">");
//		//			sTempRouter = asTableData[9].substring(0, asTableData[9].indexOf("</font>"));
//		//			sTempRouter = sTempRouter.trim();
//		//			
//		//		}
//		//		else
//		//		{
//		//			sTempRouter = oRouterStatusPageStringbuffer.substring(oRouterStatusPageStringbuffer.indexOf("WAN:"),
//		//					oRouterStatusPageStringbuffer.indexOf("Default Gateway:"));
//		//			String sIpQue = "IP Address:</td><td><font face=verdana size=2>";
//		//			sTempRouter = sTempRouter.substring(sTempRouter.indexOf(sIpQue) + sIpQue.length(), sTempRouter.length());
//		//			sTempRouter = sTempRouter.substring(0, sTempRouter.indexOf("</td>"));
//		//			sTempRouter = sTempRouter.trim();
//		//		}
//
//		return sTempRouter;
//
//	}

}

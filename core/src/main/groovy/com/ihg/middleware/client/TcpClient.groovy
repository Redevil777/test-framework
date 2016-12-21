package com.ihg.middleware.client

import groovy.util.logging.Log4j

/**
 * Created by Vasili_Ramantsou on 12/3/2014.
 */
@Log4j
class TcpClient {

    /**
     * Web Service URL.
     */
    String hostUrl

    /**
     * Web Service listening port
     */
    int portNumber
    int defaultSleepTime

    TcpClient(String hostUrl, int portNumber) {
        this.hostUrl = hostUrl
        this.portNumber = portNumber
        this.defaultSleepTime = 0
    }

    List<Integer> send(byte[] bytes) {
        try {
            List<Integer> response = []
            Socket socket = new Socket()
            socket.connect(new InetSocketAddress(hostUrl, portNumber))
            OutputStream output = socket.getOutputStream()
            if (output != null && bytes != null) {
                log.debug("Sending bytes to tcp listener: ${hostUrl}:${portNumber} " + bytes.collect { Integer.toHexString(it) })
                output.write(bytes)
                output.flush()
            }
            InputStream inputStream = socket.getInputStream()
            Thread.sleep(5000)
            while (inputStream.available() > 0) {
                response.add(inputStream.read())
            }
            log.debug("Response from ${hostUrl}:${portNumber}: " + response)
            return response
        } catch (IOException e) {
            e.printStackTrace()
        }
    }

    String send(String text) {
        Socket socket = null
        BufferedReader input = null
        DataOutputStream output = null
        log.debug("Sending message: ${text}")
        try {
            socket = new Socket(hostUrl, portNumber)
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            output = new DataOutputStream(socket.getOutputStream())
            output.writeInt(text.length())
            output.write(text.getBytes())
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to server.")
            System.exit(1)
        }
        String response = convertStreamToString(socket.getInputStream())
        input.close()
        output.close()
        socket.close()
        sleep(defaultSleepTime)
        return response
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A")
        return s.hasNext() ? s.next() : ""
    }
}

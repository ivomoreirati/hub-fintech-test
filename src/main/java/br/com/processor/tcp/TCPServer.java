package br.com.processor.tcp;

import br.com.processor.constants.TransactionResultCode;
import br.com.processor.dto.TransactionRequestDTO;
import br.com.processor.dto.TransactionResponseDTO;
import br.com.processor.services.CardTransactionService;
import br.com.processor.util.ParseJson;
import br.com.processor.util.Util;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class for use of port socket with threads for receive requests . . .
 *
 * @author Ivo Moreira
 *
 */

@Slf4j
public class TCPServer{

    static public void createServerTCP(int port, CardTransactionService cardTransactionService) {
        
        ServerSocket serverSocket;
        
        try{            
            serverSocket = new ServerSocket();

            serverSocket.setReuseAddress(true);

            InetSocketAddress address = new InetSocketAddress("localhost", port);

            serverSocket.bind(address);

            log.info("Create server TCP success: IP: "
                    + address.getAddress().getHostAddress() + " in Port: " + address.getPort());

            while(true)
                processing(serverSocket.accept(), cardTransactionService);
            
        }catch(IOException e){

            log.info("Error create Server TCP in Port: " + port + e);
        }
    }

    static public void processing(Socket socket, CardTransactionService cardTransactionService) {

        try{
            InputStreamReader instr = new InputStreamReader(socket.getInputStream());

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader bufferedReader = new BufferedReader(instr);

            String request = Util.readLinesBufferedReader(bufferedReader, "}");

            TransactionResponseDTO response_dto = null;

            if((request != null) && (request.length() > 0)){

                TransactionRequestDTO request_dto =
                        (TransactionRequestDTO) ParseJson.convertJSON_OBJ(request, TransactionRequestDTO.class);

                if(request_dto != null){
                    response_dto = cardTransactionService.processRequestTransaction(request_dto);

                }
                else response_dto =
                        CardTransactionService.createTransactionResponseDTO(null, TransactionResultCode.PROCESSING_ERROR, -1L);

            }
            else response_dto =
                    CardTransactionService.createTransactionResponseDTO(null, TransactionResultCode.PROCESSING_ERROR, -1L);

            String response = ParseJson.convertOBJ_JSON(response_dto);

            out.println(response);

            out.close();
            socket.close();

        }catch(Exception e){

            log.error("Client loss conection in Server TCP.");

            e.printStackTrace();
        }
    }
}

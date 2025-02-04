package fr.umontpellier.iut.socketwrapper.providers;

import java.io.IOException;
import java.net.ServerSocket;

public interface ServerSocketProvider {
    ServerSocket createServerSocket() throws IOException;
}
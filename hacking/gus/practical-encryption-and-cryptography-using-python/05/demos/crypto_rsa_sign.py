from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from Crypto.Signature import PKCS1_PSS
from Crypto import Random
from Crypto.Hash import SHA256
import base64


class CryptoRSA:
    PRIVATE_KEY_FILE = "private_key.pem"
    PUBLIC_KEY_FILE = "public_key.pem"

    def __init__(self):
        return

    def __save_file(self, contents, file_name):
        f = open(file_name, 'w')
        f.write(contents)
        f.close()

    def __read_file(self, file_name):
        f = open(file_name, 'r')
        contents = f.read()
        f.close()
        return contents

    def __generate_random(self):
        return Random.new().read

    def generate_keys(self):
        # Key object to generate a public & private keys with 4096 bits long
        keys = RSA.generate(4096)

        # Generate a private key
        private_key = keys.exportKey("PEM")

        # Generate a public key
        public_key = keys.publickey().exportKey("PEM")

        # Save private key to a file
        self.__save_file(private_key, self.PRIVATE_KEY_FILE)

        # Save public key to a file
        self.__save_file(public_key, self.PUBLIC_KEY_FILE)

        print "Public & Private Keys; generated and saved successfully!"

    def __sha256(self,input):
        sha256 = SHA256.new()
        sha256.update(input)
        return sha256

    def encrypt(self, cleartext, public_key_path=None):
        if public_key_path == None:
            public_key_path = self.PUBLIC_KEY_FILE

        # Create the public key object
        public_key = RSA.importKey(self.__read_file(public_key_path))
        # Create PKCS1 object
        cipher = PKCS1_OAEP.new(public_key)
        # Encrypt data
        encrypted_data = cipher.encrypt(cleartext)
        # Return encrypted & encoded data
        return base64.b64encode(encrypted_data)

    def decrypt(self, cipher_text, private_key_path=None):
        if private_key_path == None:
            private_key_path = self.PRIVATE_KEY_FILE

        # Decode before decrypting
        cipher_text = base64.b64decode(cipher_text)
        # Create private key object
        private_key = RSA.importKey(self.__read_file(private_key_path))
        # Create PKCS1 object
        cipher = PKCS1_OAEP.new(private_key)
        # Decrypt data
        decrypted_data = cipher.decrypt(cipher_text)
        # Return original cleartext data
        return decrypted_data

    def sign(self,textmessage,private_key_path=None):
        if private_key_path == None:
            private_key_path = self.PRIVATE_KEY_FILE

        # Create private key object
        private_key = RSA.importKey(self.__read_file(private_key_path))
        # Create the signature
        signature = PKCS1_PSS.new(private_key)
        return signature.sign(self.__sha256(textmessage))

    def verify(self,signed_signature,textmessage,public_key_path=None):
        if public_key_path == None:
            public_key_path = self.PUBLIC_KEY_FILE

        # Create the public key object
        public_key = RSA.importKey(self.__read_file(public_key_path))
        # Create the verifier
        verifier = PKCS1_PSS.new(public_key)
        verification = verifier.verify(self.__sha256(textmessage),signed_signature)
        print verification








# Class Test
"""CryptoRSA().generate_keys()
encrypted_data = CryptoRSA().encrypt("Hello World")
print encrypted_data
decrypted_data = CryptoRSA().decrypt(encrypted_data)
print decrypted_data"""

signed_signature = CryptoRSA().sign("Hello World")
CryptoRSA().verify(signed_signature,"Hello World")

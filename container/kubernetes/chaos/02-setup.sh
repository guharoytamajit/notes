# Source: https://gist.github.com/37b4c10aca0d1214965506a146fc3488

################################
# Defining Course Requirements #
################################

# Git: https://git-scm.com/
# GitBash (if Windows)
# kubectl: https://kubernetes.io/docs/tasks/tools/install-kubectl/
# A Docker Desktop, Minikube, GKE, EKS, AKS, or any other Kubernetes cluster (not tested)
# Helm v3.x (if EKS): https://helm.sh/docs/intro/install/
# Python v3.x: https://www.python.org/downloads
# pip: https://pip.pypa.io/en/stable/installing

############################
# Installing Chaos Toolkit #
############################

pip install -U chaostoolkit

chaos --help

# If `chaos` fails, you might want to activate the virtual environment.
# This is optional and should be executed only if `chaos --help` did not work.
source ~/.venvs/chaostk/bin/activate

# If `chaos` fails, you might want to activate the virtual environment
# This is optional and should be executed only if `chaos --help` did not work.
python3 -m venv ~/.venvs/chaostk

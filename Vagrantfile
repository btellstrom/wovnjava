# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.hostname = "wovnjava.local"
  config.vm.network "private_network", ip: "192.168.33.101"

  config.vm.provider "virtualbox" do |vb|
    vb.customize ["modifyvm", :id, "--paravirtprovider", "kvm"]
    # vb.customize ["modifyvm", :id, "--memory", "1024"]
  end

  config.vm.provision "shell", path: "provision.sh"

  config.hostmanager.enabled = true
  config.hostmanager.manage_host = true
end


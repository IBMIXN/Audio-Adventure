import os
import argparse


def deploy(name, debug=False):
  cmd = f'''sam deploy \
  --template template_{name}.yaml \
  --config-file samconfig_{name}.toml''' + \
        (" --debug" if debug else "")
  stream = os.popen(cmd)
  print(stream.read())

def build(debug=False):
  cmd = "sam build" + ("-- debug" if debug else "")
  stream = os.popen(cmd)
  print(stream.read())

if __name__ == '__main__':
  parser = argparse.ArgumentParser(description='Command line tool for sam')
  g = parser.add_mutually_exclusive_group()
  g.add_argument('-b', '--build', action='store_true', default=False, help="sam build")
  g.add_argument('-d', '--deploy', choices=["dev", 'prod'], help="deploy the project to the given stack")
  parser.add_argument('--debug', action='store_true', default=False, help="activate debug")
  args = parser.parse_args()

  if args.build:
    build(debug=args.debug)
  elif args.deploy is not None:
    deploy(args.deploy, debug=args.debug)
  else:
    print("Nope")
